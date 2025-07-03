package com.ac.derivativepricer.business;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.AdjustedGreekEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.PrimaryGreekEvent;
import com.ac.derivativepricer.process.CharArrayKey;
import com.ac.derivativepricer.process.EventLoop;
import static com.ac.derivativepricer.process.EventLoop.poll;
import com.ac.derivativepricer.process.StartableProcess;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.EventPoller.PollState;
import static com.lmax.disruptor.EventPoller.PollState.PROCESSING;
import com.lmax.disruptor.RingBuffer;

public class AdjustmentProcess implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(AdjustmentProcess.class);

    protected final RingBuffer<AdjustedGreekEvent> adjustedGreekRb;
    protected final EventLoop eventLoop;
    protected final Random rand = new Random();
    protected final IdleStrategy idleStrategy = new SleepingIdleStrategy();

    // caches and indices
    private final Map<CharArrayKey, Map<CharArrayKey, PrimaryGreekEvent>> strategyId2InstrumentId2PrimaryGreek = new HashMap<>();
    private final Map<CharArrayKey, MarketDataEvent> marketDataId2MarketData = new HashMap<>();
    private final Map<CharArrayKey, Set<CharArrayKey>> marketDataId2StrategyId = new HashMap<>();

    public AdjustmentProcess(
            RingBuffer<PrimaryGreekEvent> primaryGreekRb,
            RingBuffer<MarketDataEvent> marketDataRb,
            RingBuffer<AdjustedGreekEvent> adjustedGreekRb) {

        this.adjustedGreekRb = adjustedGreekRb;
        EventPoller<PrimaryGreekEvent> primaryGreekPoller = primaryGreekRb.newPoller();
        EventPoller<MarketDataEvent> marketDataPoller = marketDataRb.newPoller();

        this.eventLoop = new EventLoop(() -> {
            PollState marketDataPollerState = poll(marketDataPoller, this::onMarketData, logger, "MarketData");
            PollState primaryGreekPollerState = poll(primaryGreekPoller, this::onPrimaryGreek, logger, "PrimaryGreek");

            if (PROCESSING != primaryGreekPollerState
                    && PROCESSING != marketDataPollerState) {
                idleStrategy.idle();
            }
        }, AdjustmentProcess.class.getSimpleName(), logger);
    }

    @Override
    public void start() {
        this.eventLoop.start();
    }

    @Override
    public void stop() {
        this.eventLoop.stop();
    }

    public void onPrimaryGreek(PrimaryGreekEvent src) {
        CharArrayKey strategyKey = new CharArrayKey(src.getStrategyId());
        CharArrayKey instrumentKey = new CharArrayKey(src.getInstrumentId());
        CharArrayKey marketDataKey = new CharArrayKey(src.getReferenceMarketDataId());
        
        strategyId2InstrumentId2PrimaryGreek
        .computeIfAbsent(strategyKey, k -> new HashMap<>())
        .computeIfAbsent(instrumentKey, k -> new PrimaryGreekEvent())
        .set(src);
        
        marketDataId2StrategyId
        .computeIfAbsent(marketDataKey, k -> new HashSet<>())
        .add(strategyKey);
        
        publishRandom(src.getStrategyId(), src.getInstrumentId(), src.getUnderlyingId(), marketDataKey);
    }
    
    public void onMarketData(MarketDataEvent src) {
        CharArrayKey marketDataKey = new CharArrayKey(src.getMarkDataId());
        
        marketDataId2MarketData
                .computeIfAbsent(marketDataKey, k -> new MarketDataEvent())
                .set(src);

        for (CharArrayKey strategyId : marketDataId2StrategyId.getOrDefault(marketDataKey, emptySet())) {
            for (Entry<CharArrayKey, PrimaryGreekEvent> primaryGreekEntry : strategyId2InstrumentId2PrimaryGreek.getOrDefault(strategyId, emptyMap()).entrySet()) {
                publishRandom(primaryGreekEntry.getValue().getStrategyId(), primaryGreekEntry.getValue().getInstrumentId(), primaryGreekEntry.getValue().getUnderlyingId(), marketDataKey);
            }
        }
    }

    private void publishRandom(char[] strategyId, char[] instrumentId, char[] underlyingId, CharArrayKey marketDataKey) {
        if (marketDataId2MarketData.get(marketDataKey) == null) {
            return;
        }

        adjustedGreekRb.publishEvent((AdjustedGreekEvent event, long sequence) -> {
            event.setStrategyId(strategyId);
            event.setInstrumentId(instrumentId);
            event.setUnderlyingId(underlyingId);
            event.setTheo(rand.nextDouble()); // TODO implement delta gamma adjustment
            event.setUnderlyingPrice(rand.nextDouble());
        });
    }
}
