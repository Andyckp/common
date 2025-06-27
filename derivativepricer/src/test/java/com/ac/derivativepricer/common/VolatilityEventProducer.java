package com.ac.derivativepricer.common;

import java.time.LocalDate;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import static com.ac.derivativepricer.data.ExpiryVolatilityEvent.VOLATILITY_ID_SIZE;
import com.ac.derivativepricer.process.EventLoop;
import com.ac.derivativepricer.process.Process;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class VolatilityEventProducer implements Process {

    private static final Logger logger = LoggerFactory.getLogger(VolatilityEventProducer.class);
    private final EventLoop eventLoop;
    private final Random rand = new Random();

    public VolatilityEventProducer(RingBuffer<ExpiryVolatilityEvent> rb) {
        this.eventLoop = new EventLoop(() -> {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    long seq = rb.next();
                    try {
                        ExpiryVolatilityEvent event = rb.get(seq);
                        event.setVolatilityId(padOrTruncate("Vol-" + i + 1, VOLATILITY_ID_SIZE));
                        event.setExpiry(LocalDate.of(2025, 3+j, 28));
                        event.setFittingReference(25.3);
                        event.setParameter1(rand.nextDouble());
                        event.setParameter2(rand.nextDouble());
                        event.setParameter3(rand.nextDouble());
                        event.setParameter4(rand.nextDouble());
                        event.setParameter5(rand.nextDouble());
                        event.setParameter6(rand.nextDouble());
                        event.setParameter7(rand.nextDouble());
                        event.setParameter8(rand.nextDouble());
                        event.setParameter9(rand.nextDouble());
                        event.setParameter10(rand.nextDouble());
                        event.setParameter11(rand.nextDouble());
                        event.setParameter12(rand.nextDouble());
                        event.setParameter13(rand.nextDouble());
                        event.setParameter14(rand.nextDouble());
                        event.setParameter15(rand.nextDouble());
                        event.setParameter16(rand.nextDouble());
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
        }, "volatility-producer", logger);
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
