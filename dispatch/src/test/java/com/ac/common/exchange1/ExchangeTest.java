package com.ac.common.exchange1;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ExchangeTest {
    @Test
    public void tests() {
        OrderBook book = new OrderBook();

        book.placeOrder(new Order(100.0, 50, "Alice", Side.ASK));
        book.placeOrder(new Order(101.0, 30, "Bob", Side.ASK));

        List<Fill> fills = book.placeOrder(new Order(102.0, 70, "Charlie", Side.BID));
        fills.forEach(System.out::println);

        System.out.println("\nOrder Book Snapshot:");
        book.printBook();
    }
}
