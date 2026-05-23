package com.ac.common.practice2;

import java.util.*;

import static java.lang.Math.min;

public class SideQueue {
    private final Side side;
    private final TreeMap<Long, Queue<Order>> queues;

    public SideQueue(Side side) {
        this.side = side;
        if (this.side == Side.BUY) {
            queues = new TreeMap<>(Comparator.reverseOrder());
        } else {
            queues = new TreeMap<>(Comparator.naturalOrder());
        }
    }

    public void addOrder(Order order) {
        if (order == null) return;
        if (order.getQuantity() <= 0L) return;
        queues.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
    }

    public List<Trade> matches(Order incomingOrder) {
        List<Trade> trades = new ArrayList<>();

        while (incomingOrder.getQuantity() > 0L) {
            Map.Entry<Long, Queue<Order>> firstEntry = queues.firstEntry();
            if (firstEntry == null) {
                return trades;
            }

            long bestPrice = firstEntry.getKey();
            long incomingPrice = incomingOrder.getPrice();

            boolean noMatch = side == Side.SELL ? incomingPrice < bestPrice : incomingPrice > bestPrice;
            if (noMatch) {
                return trades;
            }

            Queue<Order> bestPriceQueue = firstEntry.getValue();
            Order bestOrder = bestPriceQueue.peek();
            if (bestOrder == null) {
                // defensive: remove empty level and continue
                queues.remove(bestPrice);
                continue;
            }

            long tradeQuantity = min(incomingOrder.getQuantity(), bestOrder.getQuantity());
            trades.add(new Trade(bestOrder.getPrice(), tradeQuantity));

            // adjust quantities
            incomingOrder.adjustQuantity(-tradeQuantity);
            bestOrder.adjustQuantity(-tradeQuantity);

            if (bestOrder.getQuantity() == 0L) {
                bestPriceQueue.remove();
                if (bestPriceQueue.isEmpty()) {
                    queues.remove(bestPrice);
                }
            }
        }

        return trades;
    }
}
