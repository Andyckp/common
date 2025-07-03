package com.ac.derivativepricer.common.nontransport.producer;

import java.time.LocalDate;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import static com.ac.derivativepricer.data.ExpiryVolatilityEvent.VOLATILITY_ID_SIZE;
import com.ac.derivativepricer.process.StartableProcess;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class VolatilityEventProducer implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(VolatilityEventProducer.class);
    private final Thread thread;
    private volatile boolean running = false;
    private final Random rand = new Random();

    public VolatilityEventProducer(RingBuffer<ExpiryVolatilityEvent> rb) {
        this.thread = new Thread(() -> {
            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 3; j++) {
                    long seq = rb.next();
                    try {
                        ExpiryVolatilityEvent event = rb.get(seq);
                        event.setVolatilityId(padOrTruncate("Vol-" + i, VOLATILITY_ID_SIZE));
                        event.setExpiry(LocalDate.of(2025, 3 + j, 28));
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
        }, "volatility-producer");
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
