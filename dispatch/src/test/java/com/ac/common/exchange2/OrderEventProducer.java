package com.ac.common.exchange2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.common.exchange2.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;


public class OrderEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);
    private final Random random = new Random();
    private volatile boolean running = false;
    private final Thread thread;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.thread = new Thread(() -> {
            int vol = 0;
            // while (running) {
            for (int i = 0; i<5000; i++) {
                long seq = ringBuffer.next();
                try {
                    OrderEvent order = ringBuffer.get(seq);
                    vol = i % 2 == 0 ? random.nextInt(9) + 1: vol;
                    Side side = i % 2 == 0 ? Side.BID : Side.ASK;
                    order.set(10000, vol, 
                        padOrTruncate("User" + random.nextInt(9999), 16), 
                        padOrTruncate("Instrument" + random.nextInt(9999), 16), 
                        side);
                } finally {
                    ringBuffer.publish(seq);
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
