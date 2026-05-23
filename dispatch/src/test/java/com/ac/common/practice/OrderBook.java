package com.ac.common.practice;

import java.util.List;

public class OrderBook {
    private final OrderQueues buyQueues = new OrderQueues(Side.BUY);
    private final OrderQueues sellQueues = new OrderQueues(Side.SELL);

     public List<Trade> addOrder(Side side, Order order) {
        if (side == Side.BUY) {
            List<Trade> trades = sellQueues.matches(order);
            if (order.getQuantity() > 0) {
                buyQueues.addOrder(order);
            }
            return trades;
        } else {
            List<Trade> trades = buyQueues.matches(order);
            if (order.getQuantity() > 0) {
                sellQueues.addOrder(order);
            }
            return trades;
        }
    }
}

