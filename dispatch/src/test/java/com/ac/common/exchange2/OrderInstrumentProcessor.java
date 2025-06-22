package com.ac.common.exchange2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.TimeoutException;

public class OrderInstrumentProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OrderInstrumentProcessor.class);
    private volatile boolean running = false;
    private final Random random = new Random();
    private final Thread thread;

    public OrderInstrumentProcessor(RingBuffer<OrderEvent> orderRingBuffer, RingBuffer<InstrumentEvent> instrumentRingBuffer,
                                    RingBuffer<FillEvent> fillRingBuffer, SequenceBarrier orderBarrier, SequenceBarrier instrumentBarrier) {
        thread = new Thread(() -> {
        	long orderSequence = orderRingBuffer.getCursor();
        	long instrumentSequence = instrumentRingBuffer.getCursor();
        	
        	while (running) {
        	    // try {
                try {
                    orderBarrier.waitFor(orderSequence);    
                } catch (AlertException | TimeoutException | InterruptedException e) {
                    e.printStackTrace();
                }
                
                try {
                    instrumentBarrier.waitFor(instrumentSequence);
                } catch (AlertException | TimeoutException | InterruptedException e) {
                    e.printStackTrace();
                }
        
                OrderEvent order = orderRingBuffer.get(orderSequence);
                InstrumentEvent instrument = instrumentRingBuffer.get(instrumentSequence);
        
                logger.info("Processing OrderEvent: " + order.price + ", " + order.volume);
                logger.info("Processing InstrumentEvent: " + new String(instrument.instrumentId) + ", Detail: " + new String(instrument.instrumentDetail));
        
                // Generate and publish a random FillEvent
                long fillSequence = fillRingBuffer.next();
                FillEvent fill = fillRingBuffer.get(fillSequence);
                fill.set(
                    order.price + random.nextInt(10), 
                    order.volume / 2, 
                    new String(order.userId),
                    "User" + random.nextInt(9999)
                );
                fillRingBuffer.publish(fillSequence);
                logger.info("Published FillEvent: " + fill.price + ", " + fill.volume);
        
                orderSequence++;
                instrumentSequence++;
                Thread.yield(); 
        	}
        }, "order-instrument-processor");
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
