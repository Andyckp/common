package com.ac.common.exchange2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;

public class OrderInstrumentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OrderInstrumentProcessor.class);
    private volatile boolean running = false;
    private final Random random = new Random();
    private final Thread thread;
    private final OrderBook orderBook;

    public OrderInstrumentProcessor(RingBuffer<OrderEvent> orderRingBuffer, RingBuffer<InstrumentEvent> instrumentRingBuffer,
            RingBuffer<FillEvent> fillRingBuffer) {
        this.orderBook = new OrderBook((price, volume, bidUserId, askUserId) -> {
            long fillSequence = fillRingBuffer.next();
            FillEvent fill = fillRingBuffer.get(fillSequence);
            fill.set(price, volume, bidUserId, askUserId);
            fillRingBuffer.publish(fillSequence);
        });
        thread = new Thread(() -> {
            Sequence orderGatingSequence = new Sequence();
            Sequence instrumentGatingSequence = new Sequence();
            long orderReadSeq = orderGatingSequence.get() + 1;
            long instrumentSeq = instrumentGatingSequence.get() + 1;

            orderRingBuffer.addGatingSequences(orderGatingSequence);
            instrumentRingBuffer.addGatingSequences(instrumentGatingSequence);

            SequenceBarrier orderBarrier = orderRingBuffer.newBarrier();

            while (running) {
                long orderWriteSeq = orderRingBuffer.getCursor();
                long instrumentWriteSeq = instrumentRingBuffer.getCursor();

                if (orderReadSeq > orderWriteSeq && instrumentSeq > instrumentWriteSeq) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue; 
                }

                while (orderReadSeq <= orderWriteSeq) {
                    // try {
                    //     orderBarrier.waitFor(orderWriteSeq);
                    // } catch (AlertException | InterruptedException | TimeoutException e) {
                    //     // TODO Auto-generated catch block
                    //     e.printStackTrace();
                    // }
                    OrderEvent order = orderRingBuffer.get(orderReadSeq);
                    
                    logger.info("Order: {}, {}, {}, {}", order.price, order.volume, order.side, orderReadSeq);
                    if (orderReadSeq % 1000000 == 0) {
                        logger.info("Order process count={}", orderReadSeq);
                    }

                    // Generate and publish a random FillEvent
                    // long fillSequence = fillRingBuffer.next();
                    // FillEvent fill = fillRingBuffer.get(fillSequence);
                    // fill.set(
                    //         random.nextInt(10),
                    //         random.nextInt(10),
                    //         "User" + random.nextInt(9999),
                    //         "User" + random.nextInt(9999)
                    // );
                    // fillRingBuffer.publish(fillSequence);
                    orderBook.placeOrder(order);
                    orderReadSeq = orderGatingSequence.incrementAndGet() + 1;
                }

                while (instrumentSeq <= instrumentWriteSeq) {
                    InstrumentEvent instrument = instrumentRingBuffer.get(instrumentSeq);
                    if (instrumentSeq % 1000000 == 0) {
                        logger.info("Instrument process count={}", instrumentSeq);
                    }

                    // Generate and publish a random FillEvent
                    // long fillSequence = fillRingBuffer.next();
                    // FillEvent fill = fillRingBuffer.get(fillSequence);
                    // fill.set(
                    //         random.nextInt(10),
                    //         random.nextInt(10),
                    //         "User" + random.nextInt(9999),
                    //         "User" + random.nextInt(9999)
                    // );
                    // fillRingBuffer.publish(fillSequence);

                    instrumentSeq = instrumentGatingSequence.incrementAndGet() + 1;
                }

                // try {
                //     Thread.sleep(1);
                // } catch (InterruptedException e) {
                //     // TODO Auto-generated catch block
                //     e.printStackTrace();
                // }
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

    public void print() {
        orderBook.printBook();
    }
}
