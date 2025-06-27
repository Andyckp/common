package com.ac.derivativepricer.common;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.MarketDataEvent;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;
import com.ac.derivativepricer.process.EventLoop;
import com.ac.derivativepricer.process.Process;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class MarketDataEventProducer implements Process {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataEventProducer.class);
    private final EventLoop eventLoop;
    private final Random rand = new Random();

    public MarketDataEventProducer(RingBuffer<MarketDataEvent> rb) {
        this.eventLoop = new EventLoop(() -> {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    long seq = rb.next();
                    try {
                        MarketDataEvent event = rb.get(seq);
                        event.setMarkDataId(padOrTruncate("MD-" + i + 1, MARKET_DATA_ID_SIZE));
                        event.setPrice(rand.nextDouble(23, 27));
                    } finally {
                        rb.publish(seq);
                    }
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }, "market-data-producer", logger);
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
