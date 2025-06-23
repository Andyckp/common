package com.ac.common.exchange2;

import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderBook {
    private static final Logger logger = LoggerFactory.getLogger(OrderBook.class);
    private static final int BASE_PRICE = 100;                 // $100.00
    private static final int TICKS_PER_UNIT = 100;             // $1 = 100 ticks
    private static final int PRICE_TICK = BASE_PRICE * TICKS_PER_UNIT; // 10_000
    private static final int CAPACITY = 4096;
    private static final int MID_INDEX = CAPACITY / 2;
    private static final int TICK_BASE = PRICE_TICK - MID_INDEX;

    private final Deque<OrderEvent>[] bidArray = initArray();
    private final Deque<OrderEvent>[] askArray = initArray();

    private final FillEventConsumer fillConsumer;

    interface FillEventConsumer {
        void accept(int price, int volume, char[] bidUserId, char[] askUserId);
    }

    public OrderBook(FillEventConsumer fillConsumer) {
        this.fillConsumer = fillConsumer;
    }

    @SuppressWarnings("unchecked")
    private static Deque<OrderEvent>[] initArray() {
        Deque<OrderEvent>[] arr = new ArrayDeque[CAPACITY];
        for (int i = 0; i < CAPACITY; i++) {
            arr[i] = new ArrayDeque<>();
        }
        return arr;
    }

    public void placeOrder(OrderEvent o) {
        int tick = o.price;

        // Select sides
        Deque<OrderEvent>[] restBook = o.side == Side.BID ? askArray : bidArray;
        
        // Match logic
        int start = o.side == Side.BID ? 0 : CAPACITY - 1;
        int end   = o.side == Side.BID ? CAPACITY : -1;
        int step  = o.side == Side.BID ? 1 : -1;

        for (int i = start; i != end && o.volume > 0; i += step) {
            Deque<OrderEvent> queue = restBook[i];
            if (queue.isEmpty()) continue;

            int oppTick = TICK_BASE + i;
            boolean crossed = o.side == Side.BID ? oppTick <= tick : oppTick >= tick;
            if (!crossed) break;

            while (!queue.isEmpty() && o.volume > 0) {
                OrderEvent resting = queue.peek();
                int traded = Math.min(o.volume, resting.volume);

                fillConsumer.accept(oppTick, traded,
                        o.side == Side.BID ? o.userId : resting.userId,
                        o.side == Side.ASK ? o.userId : resting.userId);

                o.volume -= traded;
                resting.volume -= traded;
                if (resting.volume == 0) {
                    queue.poll();
                }
            }
        }

        if (o.volume > 0) {
            Deque<OrderEvent>[] ownBook  = o.side == Side.BID ? bidArray : askArray;
            int idx = Math.floorMod(tick - TICK_BASE, CAPACITY);
            ownBook[idx].add(o);
        }
    }

    public void printBook() {
        logger.info("=== ORDER BOOK ===");

        logger.info("ASKS:");
        for (int i = 0; i < CAPACITY; i++) {
            Deque<OrderEvent> q = askArray[i];
            if (!q.isEmpty()) {
                int tick = TICK_BASE + i;
                int vol = q.stream().mapToInt(o -> o.volume).sum();
                logger.info("{} - {}", tick / 100.0, vol);
            }
        }

        logger.info("BIDS:");
        for (int i = CAPACITY - 1; i >= 0; i--) {
            Deque<OrderEvent> q = bidArray[i];
            if (!q.isEmpty()) {
                int tick = TICK_BASE + i;
                int vol = q.stream().mapToInt(o -> o.volume).sum();
                logger.info("{} - {}", tick / 100.0, vol);
            }
        }

        logger.info("===================");
    }
}
