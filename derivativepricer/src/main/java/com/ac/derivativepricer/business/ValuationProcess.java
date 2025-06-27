package com.ac.derivativepricer.business;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import com.ac.derivativepricer.data.InstrumentEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.StrategyEvent;
import com.ac.derivativepricer.process.EventLoop;
import static com.ac.derivativepricer.process.EventLoop.poll;
import com.ac.derivativepricer.process.Process;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.EventPoller.PollState;
import static com.lmax.disruptor.EventPoller.PollState.PROCESSING;
import com.lmax.disruptor.RingBuffer;

public abstract class ValuationProcess<T> implements Process {

    protected final RingBuffer<T> greekRb;
    protected final Random rand = new Random();
    protected final EventLoop eventLoop;

    public ValuationProcess(
            RingBuffer<InstrumentEvent> instrumentRb,
            RingBuffer<StrategyEvent> strategyRb,
            RingBuffer<ExpiryVolatilityEvent> volatilityRb,
            RingBuffer<MarketDataEvent> marketDataRb,
            RingBuffer<T> greekRb,
            String processName,
            Logger logger) {

        this.greekRb = greekRb;
        EventPoller<InstrumentEvent> instrumentPoller = instrumentRb.newPoller();
        EventPoller<StrategyEvent> strategyPoller = strategyRb.newPoller();
        EventPoller<ExpiryVolatilityEvent> volatilityPoller = volatilityRb.newPoller();
        EventPoller<MarketDataEvent> marketDataPoller = marketDataRb.newPoller();

        this.eventLoop = new EventLoop(() -> {
            // while (true) {
            PollState marketDataPollerState = poll(marketDataPoller, this::onMarketData, logger, "MarketData");
            PollState volatilityPollerState = poll(volatilityPoller, this::onExpiryVolatility, logger, "Volatility");
            PollState strategyPollerState = poll(strategyPoller, this::onStrategy, logger, "Strategy");
            PollState instrumentPollerState = poll(instrumentPoller, this::onInstrument, logger, "Instrument");
            
            if (PROCESSING != instrumentPollerState 
                && PROCESSING != strategyPollerState
                && PROCESSING != volatilityPollerState
                && PROCESSING != marketDataPollerState) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            // }
        }, processName, logger);
    }

    @Override
    public void start() {
        this.eventLoop.start();
    }

    @Override
    public void stop() {
        this.eventLoop.stop();
    }

    public void onStrategy(StrategyEvent strategyEvent) {
        publishRandom();
    }

    public void onInstrument(InstrumentEvent instrumentEvent) {
        publishRandom();
    }

    public void onExpiryVolatility(ExpiryVolatilityEvent expiryVolatilityEvent) {
        publishRandom();
    }

    public void onMarketData(MarketDataEvent marketDataEvent) {
        publishRandom();
    }

    protected abstract void publishRandom();
}
