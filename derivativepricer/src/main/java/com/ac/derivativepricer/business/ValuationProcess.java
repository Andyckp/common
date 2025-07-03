package com.ac.derivativepricer.business;

import java.time.Instant;
import static java.time.Instant.now;
import java.time.LocalDate;
import java.util.Arrays;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.slf4j.Logger;

import com.ac.derivativepricer.data.EnrichedStrategyEvent;
import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import com.ac.derivativepricer.data.InstrumentEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.StrategyEvent;
import com.ac.derivativepricer.process.CharArrayKey;
import com.ac.derivativepricer.process.EventLoop;
import static com.ac.derivativepricer.process.EventLoop.poll;
import com.ac.derivativepricer.process.StartableProcess;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.EventPoller.PollState;
import static com.lmax.disruptor.EventPoller.PollState.PROCESSING;
import com.lmax.disruptor.RingBuffer;

public abstract class ValuationProcess<T> implements StartableProcess {

    private static final int MAX_CALCULATION_TIME_PER_CYCLE = 100;

    protected final RingBuffer<T> greekRb;
    protected final EventLoop eventLoop;
    protected final Random rand = new Random(); // TODO replace with quant library
    protected final IdleStrategy idleStrategy = new SleepingIdleStrategy();

    // caches
    private final Map<CharArrayKey, EnrichedStrategyEvent> strategyId2Strategy = new HashMap<>();
    private final Map<CharArrayKey, Map<CharArrayKey, InstrumentEvent>> underlyingId2instrumentId2Instrument = new HashMap<>();
    private final Map<CharArrayKey, Map<LocalDate, ExpiryVolatilityEvent>> volatilityId2Expiry2Volatility = new HashMap<>();
    private final Map<CharArrayKey, MarketDataEvent> marketDataId2MarketData = new HashMap<>();

    // indices
    private final Map<CharArrayKey, Set<CharArrayKey>> underlyingId2StrategyId = new HashMap<>();
    private final Map<CharArrayKey, Set<CharArrayKey>> volatilityId2StrategyId = new HashMap<>();
    private final Map<CharArrayKey, Set<CharArrayKey>> marketDataId2StrategyId = new HashMap<>();

    // recalculation
    private final Set<CharArrayKey> strategyIdOfRecalculation = new LinkedHashSet<>(); // sorted according to insertion order

    // partition
    private final int partitionId;
    private final int partitionCount;

    public ValuationProcess(
            RingBuffer<InstrumentEvent> instrumentRb,
            RingBuffer<StrategyEvent> strategyRb,
            RingBuffer<ExpiryVolatilityEvent> volatilityRb,
            RingBuffer<MarketDataEvent> marketDataRb,
            RingBuffer<T> greekRb, 
            int partitionId, 
            int partitionCount,
            String processName,
            Logger logger) {

        this.greekRb = greekRb;
        this.partitionCount = partitionCount;
        this.partitionId = partitionId;
        EventPoller<InstrumentEvent> instrumentPoller = instrumentRb.newPoller();
        EventPoller<StrategyEvent> strategyPoller = strategyRb.newPoller();
        EventPoller<ExpiryVolatilityEvent> volatilityPoller = volatilityRb.newPoller();
        EventPoller<MarketDataEvent> marketDataPoller = marketDataRb.newPoller();

        this.eventLoop = new EventLoop(() -> {
            PollState marketDataPollerState = poll(marketDataPoller, this::onMarketData, logger, "MarketData");
            PollState volatilityPollerState = poll(volatilityPoller, this::onExpiryVolatility, logger, "Volatility");
            PollState strategyPollerState = poll(strategyPoller, this::onStrategy, logger, "Strategy");
            PollState instrumentPollerState = poll(instrumentPoller, this::onInstrument, logger, "Instrument");

            // reorderCalculationSet(); // For future, reorder the calculation set due to more intelligent priority rule in runtime
            
            // recalculate once after consuming and conflating all existing data
            recalculate();

            if (strategyIdOfRecalculation.isEmpty()
                    && PROCESSING != instrumentPollerState
                    && PROCESSING != strategyPollerState
                    && PROCESSING != volatilityPollerState
                    && PROCESSING != marketDataPollerState) {
                idleStrategy.idle();
            }
        }, processName + "-" + partitionId, logger);
    }

    @Override
    public void start() {
        this.eventLoop.start();
    }

    @Override
    public void stop() {
        this.eventLoop.stop();
    }

    public void onStrategy(StrategyEvent src) {
        if (Arrays.hashCode(src.getUnderlyingId()) % partitionCount == partitionId) {
            return;
        }

        CharArrayKey strategyKey = new CharArrayKey(src.getStrategyId());
        CharArrayKey underlyingKey = new CharArrayKey(src.getUnderlyingId());
        CharArrayKey volatilityKey = new CharArrayKey(src.getVolatilityId());
        CharArrayKey marketDataKey = new CharArrayKey(src.getMarketDataId());

        strategyId2Strategy
                .computeIfAbsent(strategyKey, k -> new EnrichedStrategyEvent())
                .set(src, underlyingKey, volatilityKey, marketDataKey);

        underlyingId2StrategyId.computeIfAbsent(underlyingKey, k -> new HashSet<>()).add(strategyKey);
        volatilityId2StrategyId.computeIfAbsent(volatilityKey, k -> new HashSet<>()).add(strategyKey);
        marketDataId2StrategyId.computeIfAbsent(marketDataKey, k -> new HashSet<>()).add(strategyKey);

        strategyIdOfRecalculation.add(strategyKey);
    }

    public void onInstrument(InstrumentEvent src) {
        if (Arrays.hashCode(src.getUnderlyingId()) % partitionCount == partitionId) {
            return;
        }
        CharArrayKey instrumentKey = new CharArrayKey(src.getInstrumentId());
        CharArrayKey underlyingKey = new CharArrayKey(src.getUnderlyingId());

        underlyingId2instrumentId2Instrument
                .computeIfAbsent(underlyingKey, k -> new HashMap<>())
                .computeIfAbsent(instrumentKey, k -> new InstrumentEvent())
                .set(src);

        strategyIdOfRecalculation.addAll(underlyingId2StrategyId.getOrDefault(underlyingKey, emptySet()));
    }

    public void onExpiryVolatility(ExpiryVolatilityEvent src) {
        CharArrayKey volatilityKey = new CharArrayKey(src.getVolatilityId());
        LocalDate expiry = src.getExpiry();

        volatilityId2Expiry2Volatility
                .computeIfAbsent(volatilityKey, k -> new HashMap<>())
                .computeIfAbsent(expiry, k -> new ExpiryVolatilityEvent())
                .set(src);

        strategyIdOfRecalculation.addAll(volatilityId2StrategyId.getOrDefault(volatilityKey, emptySet()));
    }

    public void onMarketData(MarketDataEvent src) {
        CharArrayKey marketDataKey = new CharArrayKey(src.getMarkDataId());

        marketDataId2MarketData
                .computeIfAbsent(marketDataKey, k -> new MarketDataEvent())
                .set(src);

        strategyIdOfRecalculation.addAll(marketDataId2StrategyId.getOrDefault(marketDataKey, emptySet()));
    }

    private void recalculate() {
        java.util.Iterator<CharArrayKey> it = strategyIdOfRecalculation.iterator();
        if (!it.hasNext()) {
            return;
        }

        Instant start = now();
        while (it.hasNext()) {
            CharArrayKey strategyId = it.next();
            EnrichedStrategyEvent strategy = strategyId2Strategy.get(strategyId);
            MarketDataEvent marketData = marketDataId2MarketData.get(strategy.getMarketDataKey());
            Map<LocalDate, ExpiryVolatilityEvent> volatility = volatilityId2Expiry2Volatility.getOrDefault(strategy.getVolatilityKey(), emptyMap());
            Map<CharArrayKey, InstrumentEvent> instruments = underlyingId2instrumentId2Instrument.getOrDefault(strategy.getUnderlyingKey(), emptyMap());

            it.remove();

            if (marketData == null || volatility.isEmpty() || instruments.isEmpty()) {
                return; // not all dependences are existed
            }

            for (Entry<CharArrayKey, InstrumentEvent> e : instruments.entrySet()) {
                publishRandom(strategy.getStrategyId(), e.getValue().getInstrumentId(), strategy.getUnderlyingId(), strategy.getVolatilityId(), strategy.getMarketDataId());
            }

            if (now().toEpochMilli() - start.toEpochMilli() > MAX_CALCULATION_TIME_PER_CYCLE) {
                // start next cycle and poll latest data in order to avoid "everlasting catching up" with "conflating more"
                return;
            }
        }
    }

    protected abstract void publishRandom(char[] strategyId, char[] cs, char[] underlyingId, char[] volatilityId, char[] marketDataId);
}
