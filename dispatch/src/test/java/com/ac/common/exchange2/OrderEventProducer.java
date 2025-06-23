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
            while (running) {
                long seq = ringBuffer.next();
                OrderEvent order = ringBuffer.get(seq);
                order.set(
                    random.nextInt(1000),
                    random.nextInt(100),
                    "User" + random.nextInt(9999),
                    "Instrument" + random.nextInt(9999),
                    random.nextBoolean() ? Side.BUY : Side.SELL
                );
                ringBuffer.publish(seq);
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
