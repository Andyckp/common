package com.ac.common.exchange2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;


public class OrderEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);
    private final Random random = new Random();
    private volatile boolean running = false;
    private final Thread thread;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.thread = new Thread(() -> {
            // while (running) {
            int vol;
            for (int i = 0; i<9999; i++) {
                long seq = ringBuffer.next();
                OrderEvent order = ringBuffer.get(seq);
                // vol = i % 2 == 0 ? random.nextInt(9) + 1: vol;
                vol = random.nextInt(9) + 1;
                Side side = random.nextBoolean() ? Side.BID : Side.ASK;
                        // seq % 2 == 0 ? Side.BID : Side.ASK;
                order.set(// random.nextInt(1000),
                    10000,
                    // random.nextInt(5),
                    vol,
                    "User" + random.nextInt(9999),
                    "Instrument" + random.nextInt(9999), 
                    side);
                // logger.info("Order: {}, {}, {}, {}", order.price, order.volume, order.side, seq);
                ringBuffer.publish(seq);
                // logger.info("Order: {}, {}, {}, {}", order.price, order.volume, order.side, seq);
                
                // IMPORTANT, cannot remove, otherwise either bid or ask price will be zero, not sure why
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (seq % 1000000 == 0) {
                    logger.info("Order produce seq={}", seq);
                }
            }
        }, "order-producer");
    }

    public void start() {
        if (running) {
            return;
        } 
        running = true;
        thread.start();
    }

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
