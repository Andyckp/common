package com.ac.common.exchange2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;

public class InstrumentEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentEventProducer.class);
    private final Random random = new Random();
    private final Thread thread;
    private volatile boolean running = false;
    private int count = 0;

    public InstrumentEventProducer(RingBuffer<InstrumentEvent> ringBuffer) {
        this.thread = new Thread(() -> {
        	while (running) {
        	    long sequence = ringBuffer.next();
        	    // try {
        	        InstrumentEvent instrument = ringBuffer.get(sequence);
        	        instrument.set("Instrument" + random.nextInt(9999), "Detail" + random.nextInt(9999));
        	        ringBuffer.publish(sequence);
        	        count++;
                    if (count % 1000000 == 0) {
                        logger.info("Instrument produce count={}", count);
                    }
                    // Thread.sleep(1); 
        	    // } catch (InterruptedException e) {
        	    //     Thread.currentThread().interrupt();
        	    // }
        	}
        }, "instrument-producer");
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
