package com.ac.common.exchange2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.EventPoller.PollState;
import static com.lmax.disruptor.EventPoller.PollState.PROCESSING;
import com.lmax.disruptor.RingBuffer;

public class OrderInstrumentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OrderInstrumentProcessor.class);
    private volatile boolean running = false;
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
        this.thread = new Thread(() -> {
            EventPoller<OrderEvent> orderPoller = orderRingBuffer.newPoller();
            EventPoller<InstrumentEvent> instrumentPoller = instrumentRingBuffer.newPoller();
            while (running) {
                PollState orderPollerState = PROCESSING;
                PollState instrumentPollerState = PROCESSING;
                try {
                    orderPollerState = orderPoller.poll((e, seq, endOfBatch) -> {
                        if (e != null) {
                            logger.info("Order: {}, {}, {}, {}", e.getPrice(), e.getVolume(), e.getSide(), seq);
                            if (seq % 1000 == 0) {
                                logger.info("Order process count={}", seq);
                            }
                            orderBook.placeOrder(e.getPrice(), e.getVolume(), e.getUserId(), e.getInstrumentId(), e.getSide());
                            return true; 
                        }
                        return false;
                    });
                } catch (Exception e) {
                    logger.error("Error processing order: {}", e.getMessage(), e);
                    continue;
                }

                try {
                    instrumentPollerState = instrumentPoller.poll((e, seq, endOfBatch) -> {
                        if (e != null) {
                            // InstrumentEvent instrument = instrumentRingBuffer.get(seq);
                            if (seq % 1000 == 0) {
                                logger.info("Instrument process count={}", seq);
                            }
                            return true; 
                        }
                        return false;
                    });
                } catch (Exception e) {
                    logger.error("Error processing instrument: {}", e.getMessage(), e);
                    continue;
                }

                // if (PROCESSING != orderPollerState && PROCESSING != instrumentPollerState) {
                //     try {
                //         TimeUnit.MILLISECONDS.sleep(1);
                //     } catch (InterruptedException e) {
                //         Thread.currentThread().interrupt();
                //         break;
                //     }
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
        // orderBook.printBook();
    }
}
