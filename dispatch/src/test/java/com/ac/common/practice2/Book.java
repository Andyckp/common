package com.ac.common.practice2;

import java.util.List;

public class Book {
    private final SideQueue buyQueue = new SideQueue(Side.BUY);
    private final SideQueue sellQueue = new SideQueue(Side.SELL);

    private List<Trade> placeOrder(OrderRequest orderRequest) {
        Order incomingOrder = new Order(orderRequest.price(), orderRequest.quantity());
        final List<Trade> trades;
        if (orderRequest.side() == Side.BUY) {
            trades = sellQueue.matches(incomingOrder);
            if (incomingOrder.getQuantity() > 0L) {
                buyQueue.addOrder(incomingOrder);
            }
        } else {
            trades = buyQueue.matches(incomingOrder);
            if (incomingOrder.getQuantity() > 0L) {
                sellQueue.addOrder(incomingOrder);
            }
        }
        return trades;
    }
}
