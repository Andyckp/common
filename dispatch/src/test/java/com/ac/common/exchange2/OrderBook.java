package com.ac.common.exchange2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.common.exchange2.CircularBuffer.BUFFER_SIZE_1;
import static com.ac.common.exchange2.CircularBuffer.BUFFER_SIZE_2;

public class OrderBook {
    private static final Logger logger = LoggerFactory.getLogger(OrderBook.class);
    private static final int BASE_PRICE = 100;                 // $100.00
    private static final int TICKS_PER_UNIT = 100;             // $1 = 100 ticks
    private static final int PRICE_TICK = BASE_PRICE * TICKS_PER_UNIT; // 10_000
    // private static final int CAPACITY = 4096;
    // private static final int MID_INDEX = CAPACITY / 2;
    // private static final int TICK_BASE = PRICE_TICK - MID_INDEX;

    private final CircularBuffer bidArray = new CircularBuffer(); 
    private final CircularBuffer askArray = new CircularBuffer();

    private final FillEventConsumer fillConsumer;

    interface FillEventConsumer {
        void accept(int price, int volume, char[] bidUserId, char[] askUserId);
    }

    public OrderBook(FillEventConsumer fillConsumer) {
        this.fillConsumer = fillConsumer;
    }

    // @SuppressWarnings("unchecked")
    // private static Deque<OrderEvent>[] initArray() {
    //     Deque<OrderEvent>[] arr = new ArrayDeque[CAPACITY];
    //     for (int i = 0; i < CAPACITY; i++) {
    //         arr[i] = new ArrayDeque<>();
    //     }
    //     return arr;
    // }

    // public void placeOrder(int price, int volume, char[] userId, char[] instrumentId, Side side) {
    //     try {
    //         // Select sides
    //         Deque<OrderEvent>[] restBook = side == Side.BID ? askArray : bidArray;
            
    //         // Match logic
    //         int start = side == Side.BID ? 0 : CAPACITY - 1;
    //         int end   = side == Side.BID ? CAPACITY : -1;
    //         int step  = side == Side.BID ? 1 : -1;
    
    //         for (int i = start; i != end && volume > 0; i += step) {
        //             Deque<OrderEvent> queue = restBook[i];
        //             if (queue.isEmpty()) continue;
        
        //             int oppTick = TICK_BASE + i;
        //             boolean crossed = side == Side.BID ? oppTick <= price : oppTick >= price;
        //             if (!crossed) break;
        
        //             while (!queue.isEmpty() && volume > 0) {
            //                 OrderEvent resting = queue.peek();
            //                 int traded = Math.min(volume, resting.getVolume());
            
            //                 fillConsumer.accept(oppTick, traded,
            //                         side == Side.BID ? userId : resting.getUserId(),
            //                         side == Side.ASK ? userId : resting.getUserId());
            
            //                 volume -= traded;
            //                 resting.setVolume(resting.getVolume() - traded);
            //                 if (resting.getVolume() == 0) {
                //                     queue.poll();
                //                 }
                //             }
                //         }
                
                //         if (volume > 0) {
                    //             Deque<OrderEvent>[] ownBook  = side == Side.BID ? bidArray : askArray;
                    //             int idx = Math.floorMod(price - TICK_BASE, CAPACITY);
                    //             OrderEvent o = new OrderEvent();
                    //             o.set(price, volume, userId, instrumentId, side);
                    //             ownBook[idx].add(o);
                    //         }
                    //     } catch (Exception e) {
                        //         e.printStackTrace();
                        //     }
                        // }
                        
    public void placeOrder(int price, int volume, char[] userId, char[] instrumentId, Side side) {
        try {
            // Select sides
            CircularBuffer restBook = side == Side.BID ? askArray : bidArray;

            if (restBook.lvl1MinSeq != Integer.MAX_VALUE && restBook.lvl1MaxSeq != Integer.MIN_VALUE) {
            
            
                // Match logic
                // int start = side == Side.BID ? restBook.lvl1MinSeq : restBook.lvl1MaxSeq;
                // int end   = side == Side.BID ? restBook.lvl1MaxSeq : restBook.lvl1MinSeq;
                // int start = side == Side.BID ? Math.max(restBook.lvl1MinSeq, 0) : Math.min(restBook.lvl1MaxSeq, BUFFER_SIZE_1 - 1);
                // int end   = side == Side.BID ? Math.min(restBook.lvl1MaxSeq, BUFFER_SIZE_1) : Math.max(restBook.lvl1MinSeq, -1);
                int start = side == Side.BID ? restBook.lvl1MinSeq : restBook.lvl1MaxSeq;
                int end   = side == Side.BID ? restBook.lvl1MaxSeq : restBook.lvl1MinSeq;
                        // int start = side == Side.BID ? 0 : CAPACITY - 1;
                        // int end   = side == Side.BID ? CAPACITY : -1;
                        // int start = side == Side.BID ? 0 : CAPACITY - 1;
                        // int end   = side == Side.BID ? CAPACITY : -1;
                int step  = side == Side.BID ? 1 : -1;

                for (int i = start; i <= end && volume > 0; i += step) {
                    CircularBuffer queue = restBook;
                    if (!queue.occupied[i % CircularBuffer.BUFFER_SIZE_1]) continue;

                    // int oppTick = TICK_BASE + i;
                    int oppTick = i;
                    boolean crossed = side == Side.BID ? oppTick <= price : oppTick >= price;
                    if (!crossed) break;

                    while (volume > 0) {
                        OrderEvent resting = queue.buffer[i % BUFFER_SIZE_1][queue.lvl2MinSeqs[i % BUFFER_SIZE_1] % BUFFER_SIZE_2];
                        int traded = Math.min(volume, resting.getVolume());

                        fillConsumer.accept(oppTick, traded,
                                side == Side.BID ? userId : resting.getUserId(),
                                side == Side.ASK ? userId : resting.getUserId());

                        volume -= traded;
                        resting.setVolume(resting.getVolume() - traded);
                        if (resting.getVolume() == 0) {
                            if (++queue.lvl2MinSeqs[i % BUFFER_SIZE_2] > queue.lvl2MaxSeqs[i % BUFFER_SIZE_2]) {
                                restBook.removeLevel1(i);
                                break;
                            }
                        }
                    }
                }
            }

            if (volume > 0) {
                CircularBuffer ownBook = side == Side.BID ? bidArray : askArray;
                ownBook.add(price, price, volume, userId, instrumentId, side);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void printBook() {
    //     logger.info("=== ORDER BOOK ===");

    //     logger.info("ASKS:");
    //     for (int i = 0; i < CAPACITY; i++) {
    //         Deque<OrderEvent> q = askArray.buffer[i];
    //         if (!q.isEmpty()) {
    //             int tick = TICK_BASE + i;
    //             for (OrderEvent o : q) {
    //                 logger.info("{} - {}", tick / 100.0, o.getVolume());
    //             }
    //             // int vol = q.stream().mapToInt(o -> volume).sum();
    //             // logger.info("{} - {}", tick / 100.0, vol);
    //         }
    //     }

    //     logger.info("BIDS:");
    //     for (int i = CAPACITY - 1; i >= 0; i--) {
    //         Deque<OrderEvent> q = bidArray[i];
    //         if (!q.isEmpty()) {
    //             int tick = TICK_BASE + i;
    //             int vol = q.stream().mapToInt(o -> o.getVolume()).sum();
    //             logger.info("{} - {}", tick / 100.0, vol);
    //         }
    //     }

    //     logger.info("===================");
    // }
}
