package com.ac.common.exchange2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;

public class OrderInstrumentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OrderInstrumentProcessor.class);
    private volatile boolean running = false;
    private final Random random = new Random();
    private final Thread thread;
    private int orderCount = 0;
    private int instrumentCount = 0;

    public OrderInstrumentProcessor(RingBuffer<OrderEvent> orderRingBuffer, RingBuffer<InstrumentEvent> instrumentRingBuffer,
            RingBuffer<FillEvent> fillRingBuffer) {
        thread = new Thread(() -> {
            Sequence orderGatingSequence = new Sequence();
            Sequence instrumentGatingSequence = new Sequence();
            long orderSequence = orderGatingSequence.get();
            long instrumentSequence = instrumentGatingSequence.get();

            orderRingBuffer.addGatingSequences(orderGatingSequence);
            instrumentRingBuffer.addGatingSequences(instrumentGatingSequence);

            while (running) {
                long orderWritePosition = orderRingBuffer.getCursor();
                long instrumentWritePosition = instrumentRingBuffer.getCursor();

                boolean hasOrder = orderWritePosition > orderSequence;
                boolean hasInstrument = instrumentWritePosition > instrumentSequence;
                if (!hasOrder && !hasInstrument) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue; 
                }

                if (hasOrder) {
                    OrderEvent order = orderRingBuffer.get(orderSequence);
                    // logger.info("Processing OrderEvent: " + order.price + ", " + order.volume);

                    // Generate and publish a random FillEvent
                    long fillSequence = fillRingBuffer.next();
                    FillEvent fill = fillRingBuffer.get(fillSequence);
                    fill.set(
                            random.nextInt(10),
                            random.nextInt(10),
                            "User" + random.nextInt(9999),
                            "User" + random.nextInt(9999)
                    );
                    fillRingBuffer.publish(fillSequence);
                    orderCount++;
                    if (orderCount % 1000000 == 0) {
                        logger.info("Order process count={}", orderCount);
                    }

                    orderSequence = orderGatingSequence.incrementAndGet();
                }

                if (hasInstrument) {
                    InstrumentEvent instrument = instrumentRingBuffer.get(instrumentSequence);
                    // logger.info("Processing InstrumentEvent: " + new String(instrument.instrumentId) + ", Detail: " + new String(instrument.instrumentDetail));

                    // Generate and publish a random FillEvent
                    long fillSequence = fillRingBuffer.next();
                    FillEvent fill = fillRingBuffer.get(fillSequence);
                    fill.set(
                            random.nextInt(10),
                            random.nextInt(10),
                            "User" + random.nextInt(9999),
                            "User" + random.nextInt(9999)
                    );
                    fillRingBuffer.publish(fillSequence);
                    instrumentCount++;
                    if (instrumentCount % 1000000 == 0) {
                        logger.info("Instrument process count={}", instrumentCount);
                    }

                    instrumentSequence = instrumentGatingSequence.incrementAndGet();
                }
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
