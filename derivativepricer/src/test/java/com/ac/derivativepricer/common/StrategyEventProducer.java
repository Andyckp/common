package com.ac.derivativepricer.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.data.ExpiryVolatilityEvent.VOLATILITY_ID_SIZE;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;
import com.ac.derivativepricer.data.StrategyEvent;
import static com.ac.derivativepricer.data.StrategyEvent.STRATEGY_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;
import com.ac.derivativepricer.process.EventLoop;
import com.ac.derivativepricer.process.Process;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class StrategyEventProducer implements Process {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataEventProducer.class);
    private final EventLoop eventLoop;

    public StrategyEventProducer(RingBuffer<StrategyEvent> rb) {
        this.eventLoop = new EventLoop(() -> {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    long seq = rb.next();
                    try {
                        StrategyEvent event = rb.get(seq);
                        event.setStrategyId(padOrTruncate("Strat-" + i + 1, STRATEGY_ID_SIZE));
                        event.setUnderlyingId(padOrTruncate("UL-" + j + 1, UNDERLYLING_ID_SIZE));
                        event.setVolatilityId(padOrTruncate("Vol-" + j + 1, VOLATILITY_ID_SIZE));
                        event.setMarketDataId(padOrTruncate("MD-" + j + 1, MARKET_DATA_ID_SIZE));
                    } finally {
                        rb.publish(seq);
                    }
                }
            }
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }, "strategy-producer", logger);
    }

    @Override
    public void start() {
        this.eventLoop.start();
    }

    @Override
    public void stop() {
        this.eventLoop.stop();
    }
}
