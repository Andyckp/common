package com.ac.common.exchange;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class OrderBook {
    private static final int BASE_PRICE = 100;                 // $100.00
    private static final int TICKS_PER_UNIT = 100;             // $1 = 100 ticks
    static final int PRICE_TICK = BASE_PRICE * TICKS_PER_UNIT; // 10_000
    static final int CAPACITY = 4096;
    private static final int MID_INDEX = CAPACITY / 2;
    private static final int TICK_BASE = PRICE_TICK - MID_INDEX;

    private final Deque<Order>[] bidArray = initArray();
    private final Deque<Order>[] askArray = initArray();

    @SuppressWarnings("unchecked")
    private static Deque<Order>[] initArray() {
        Deque<Order>[] arr = new ArrayDeque[CAPACITY];
        for (int i = 0; i < CAPACITY; i++) {
            arr[i] = new ArrayDeque<>();
        }
        return arr;
    }

    public List<Fill> placeOrder(Order o) {
        List<Fill> fills = Collections.emptyList();
        int tick = o.priceTick;

        // Select sides
        Deque<Order>[] restBook = o.side == Side.BID ? askArray : bidArray;
        
        // Match logic
        int start = o.side == Side.BID ? 0 : CAPACITY - 1;
        int end   = o.side == Side.BID ? CAPACITY : -1;
        int step  = o.side == Side.BID ? 1 : -1;

        for (int i = start; i != end && o.volume > 0; i += step) {
            Deque<Order> queue = restBook[i];
            if (queue.isEmpty()) continue;

            int oppTick = TICK_BASE + i;
            boolean crossed = o.side == Side.BID ? oppTick <= tick : oppTick >= tick;
            if (!crossed) break;

            while (!queue.isEmpty() && o.volume > 0) {
                Order resting = queue.peek();
                int traded = Math.min(o.volume, resting.volume);

                if (fills.isEmpty()) {
                    fills = new ArrayList<>();
                }
                fills.add(new Fill(oppTick, traded,
                        o.side == Side.BID ? o.userId : resting.userId,
                        o.side == Side.ASK ? o.userId : resting.userId));

                o.volume -= traded;
                resting.volume -= traded;
                if (resting.volume == 0) {
                    queue.poll();
                }
            }
        }

        
        if (o.volume > 0) {
            Deque<Order>[] ownBook  = o.side == Side.BID ? bidArray : askArray;
            int idx = Math.floorMod(tick - TICK_BASE, CAPACITY);
            ownBook[idx].add(o);
        }

        return fills;
    }

    public void printBook() {
        System.out.println("=== ORDER BOOK ===");

        System.out.println("ASKS:");
        for (int i = 0; i < CAPACITY; i++) {
            Deque<Order> q = askArray[i];
            if (!q.isEmpty()) {
                int tick = TICK_BASE + i;
                int vol = q.stream().mapToInt(o -> o.volume).sum();
                System.out.printf("  %.2f - %d units%n", tick / 100.0, vol);
            }
        }

        System.out.println("BIDS:");
        for (int i = CAPACITY - 1; i >= 0; i--) {
            Deque<Order> q = bidArray[i];
            if (!q.isEmpty()) {
                int tick = TICK_BASE + i;
                int vol = q.stream().mapToInt(o -> o.volume).sum();
                System.out.printf("  %.2f - %d units%n", tick / 100.0, vol);
            }
        }

        System.out.println("===================");
    }
}
