package com.ac.common.exchange1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;


public class OrderBook {
    // Time-priority queue: price -> list of orders
    private final TreeMap<Double, Queue<Order>> bids = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Double, Queue<Order>> asks = new TreeMap<>();

    public List<Fill> placeOrder(Order incoming) {
        List<Fill> fills = new ArrayList<>();
        TreeMap<Double, Queue<Order>> book = incoming.side == Side.BID ? asks : bids;

        Iterator<Map.Entry<Double, Queue<Order>>> it = book.entrySet().iterator();
        while (it.hasNext() && incoming.volume > 0) {
            Map.Entry<Double, Queue<Order>> entry = it.next();
            double priceLevel = entry.getKey();

            // Match condition: price crosses
            boolean canMatch = (incoming.side == Side.BID && incoming.price >= priceLevel) ||
                               (incoming.side == Side.ASK && incoming.price <= priceLevel);

            if (!canMatch) break;

            Queue<Order> ordersAtLevel = entry.getValue();
            while (!ordersAtLevel.isEmpty() && incoming.volume > 0) {
                Order resting = ordersAtLevel.peek();

                int tradedVolume = Math.min(incoming.volume, resting.volume);
                incoming.volume -= tradedVolume;
                resting.volume -= tradedVolume;

                fills.add(new Fill(priceLevel, tradedVolume,
                        incoming.side == Side.BID ? incoming.userId : resting.userId,
                        incoming.side == Side.ASK ? incoming.userId : resting.userId));

                if (resting.volume == 0) {
                    ordersAtLevel.poll();
                }
            }
            if (ordersAtLevel.isEmpty()) it.remove();
        }

        // Any unfilled volume goes to book
        if (incoming.volume > 0) {
            TreeMap<Double, Queue<Order>> targetBook = incoming.side == Side.BID ? bids : asks;
            targetBook.computeIfAbsent(incoming.price, k -> new LinkedList<>()).add(incoming);
        }

        return fills;
    }

    public void printBook() {
        System.out.println("ASKS:");
        asks.forEach((price, q) -> System.out.printf("  $%.2f -> %d units\n", price,
                q.stream().mapToInt(o -> o.volume).sum()));

        System.out.println("BIDS:");
        bids.forEach((price, q) -> System.out.printf("  $%.2f -> %d units\n", price,
                q.stream().mapToInt(o -> o.volume).sum()));
    }
}

