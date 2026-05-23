package com.ac.common.practice;

import java.util.*;

public class OrderQueues {
    private final Side side;
    private final TreeMap<Integer, Queue<Order>> queues;

    public OrderQueues(Side side) {
        this.side = side;
        this.queues = new TreeMap<>((o1, o2) -> {
            if (OrderQueues.this.side == Side.BUY) {
                return Integer.compare(o2, o1);
            } else {
                return Integer.compare(o1, o2);
            }
        });
    }

    public void addOrder(Order order) {
        queues.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
    }

    public List<Trade> matches(Order incomingOrder) {

        List<Trade> trades = new ArrayList<>();

        while (incomingOrder.getQuantity() > 0) {
            Map.Entry<Integer, Queue<Order>> firstEntry = queues.firstEntry();
            if (firstEntry == null) {
                return trades;
            }

            final int bestPrice = firstEntry.getKey();
            if (side == Side.BUY) {
                if (bestPrice < incomingOrder.getPrice()) {
                    return trades;
                }
            } else {
                if (bestPrice > incomingOrder.getPrice()) {
                    return trades;
                }
            }

            Queue<Order> firstQueue = firstEntry.getValue();
            Order matchedOrder = firstQueue.peek();

            if (matchedOrder == null) {
                queues.remove(firstEntry.getKey());
                continue;
            }

            final int tradeQuantity;
            if (matchedOrder.getQuantity() <= incomingOrder.getQuantity()) {
                tradeQuantity = matchedOrder.getQuantity();
                trades.add(new Trade(matchedOrder.getPrice(), tradeQuantity));

                incomingOrder.setQuantity(incomingOrder.getQuantity() - tradeQuantity);
                firstQueue.remove();

                if (firstQueue.isEmpty()) {
                    queues.remove(firstEntry.getKey());
                }

            } else {
                tradeQuantity = incomingOrder.getQuantity();
                trades.add(new Trade(matchedOrder.getPrice(), tradeQuantity));

                incomingOrder.setQuantity(0);
                matchedOrder.setQuantity(matchedOrder.getQuantity() - tradeQuantity);
            }
        }

        return trades;
    }

    @Override
    public String toString() {
        return "OrderQueues{" +
            "side=" + side +
            ", queues=" + queues +
            '}';
    }
}
