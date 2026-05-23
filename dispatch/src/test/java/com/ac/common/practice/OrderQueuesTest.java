package com.ac.common.practice;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderQueuesTest {
    private static final Logger logger = LoggerFactory.getLogger(OrderQueuesTest.class);

    @Test
    public void when_add_order_to_buy_queues_then_order_added() {
        OrderQueues orderQueues = new OrderQueues(Side.BUY);
        orderQueues.addOrder(new Order(1, 100));
        orderQueues.addOrder(new Order(1, 200));
        orderQueues.addOrder(new Order(2, 200));

        logger.info("Order queue: {}", orderQueues);
    }

    @Test
    public void when_add_order_to_sell_queues_then_order_added() {
        OrderQueues orderQueues = new OrderQueues(Side.SELL);
        orderQueues.addOrder(new Order(1, 100));
        orderQueues.addOrder(new Order(1, 200));
        orderQueues.addOrder(new Order(2, 200));

        logger.info("Order queue: {}", orderQueues);
    }

    @Test
    public void when_find_order_from_buy_queues_then_order_found() {
        OrderQueues orderQueues = new OrderQueues(Side.BUY);
        orderQueues.addOrder(new Order(1, 100));
        orderQueues.addOrder(new Order(1, 200));
        orderQueues.addOrder(new Order(2, 200));

        List<Trade> trades = orderQueues.matches(new Order(0, 0));;
        logger.info("Order queue: {}", orderQueues);

        logger.info("Trades: {}", trades);
    }
}
