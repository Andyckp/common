package com.ac.derivativepricer.common.nontransport.producer;

import java.util.Random;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.MarketDataEvent;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;
import com.ac.derivativepricer.process.EventLoop;
import com.ac.derivativepricer.process.StartableProcess;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class MarketDataEventProducer implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataEventProducer.class);
    private final EventLoop eventLoop;
    private final Random rand = new Random();
    private final IdleStrategy idleStrategy2s = new SleepingIdleStrategy(2_000_000_000);

    public MarketDataEventProducer(RingBuffer<MarketDataEvent> rb) {
        this.eventLoop = new EventLoop(() -> {
            for (int i = 1; i <= 2; i++) {
                long seq = rb.next();
                try {
                    MarketDataEvent event = rb.get(seq);
                    event.setMarkDataId(padOrTruncate("MD-" + i, MARKET_DATA_ID_SIZE));
                    event.setPrice(rand.nextDouble(25, 26));
                } finally {
                    rb.publish(seq);
                }
            }
            idleStrategy2s.idle();
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
