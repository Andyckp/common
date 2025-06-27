package com.ac.derivativepricer.common.nontransport.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.data.ExpiryVolatilityEvent.VOLATILITY_ID_SIZE;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;
import com.ac.derivativepricer.data.StrategyEvent;
import static com.ac.derivativepricer.data.StrategyEvent.STRATEGY_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;
import com.ac.derivativepricer.process.StartableProcess;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class StrategyEventProducer implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataEventProducer.class);
    private final Thread thread;
    private volatile boolean running = false;

    public StrategyEventProducer(RingBuffer<StrategyEvent> rb) {
        this.thread = new Thread(() -> {
            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 2; j++) {
                    long seq = rb.next();
                    try {
                        StrategyEvent event = rb.get(seq);
                        event.setStrategyId(padOrTruncate("S-" + j + "-" + i, STRATEGY_ID_SIZE));
                        event.setUnderlyingId(padOrTruncate("UL-" + j, UNDERLYLING_ID_SIZE));
                        event.setVolatilityId(padOrTruncate("Vol-" + j, VOLATILITY_ID_SIZE));
                        event.setMarketDataId(padOrTruncate("MD-" + j, MARKET_DATA_ID_SIZE));
                    } finally {
                        rb.publish(seq);
                    }
                }
            }
        }, "strategy-producer");
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread.start();
    }

    @Override
    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
