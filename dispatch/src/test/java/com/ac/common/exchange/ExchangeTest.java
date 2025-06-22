package com.ac.common.exchange;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ExchangeTest {
    @Test
    public void tests() {
        OrderBook book = new OrderBook();

        book.placeOrder(new Order(10000, 50, "Alice", Side.ASK));
        book.placeOrder(new Order(10100, 30, "Bob", Side.ASK));

        List<Fill> fills = book.placeOrder(new Order(10200, 70, "Charlie", Side.BID));
        fills.forEach(System.out::println);

        System.out.println("\nOrder Book Snapshot:");
        book.printBook();
    }

    // @Test
    // void testPriceMoveUpBeyondCapacity() {
    //     OrderBook book = new OrderBook();
    //     int startPrice = 10_000; // $100.00 in tick units
    //     int stepSize = 1; // Move price up by 1 tick per order

    //     // Place orders step by step, increasing price
    //     for (int i = 0; i < OrderBook.CAPACITY; i++) {
    //         int priceTick = startPrice + (i * stepSize);
    //         Order order = new Order(priceTick, 10, "User" + i, Side.BID);
    //         book.placeOrder(order);
    //     }

    //     // Ensure book correctly tracks min/max bid ticks
    //     // assertEquals(startPrice, book.getBidMinTick());
    //     // assertEquals(startPrice + OrderBook.CAPACITY - 1, book.getBidMaxTick());

    //     // Try placing one more order beyond capacity
    //     int overflowPriceTick = startPrice + OrderBook.CAPACITY;
    //     Order overflowOrder = new Order(overflowPriceTick, 10, "OverflowUser", Side.BID);

    //     Exception exception = assertThrows(IllegalStateException.class, () -> {
    //         book.placeOrder(overflowOrder);
    //     });

    //     assertTrue(exception.getMessage().contains("Price range exceeded capacity"));
    // }

    // @Test
    // void testAskPlacementAndBidClearingWithDynamicStart() {
    //     OrderBook book = new OrderBook();
    //     int startPrice = OrderBook.PRICE_TICK; // $100.00 in tick units
    //     int stepSize = 1; // Price step per order

    //     // assertDoesNotThrow(() -> {
    //         for (int round = 0; round < 3; round++) {
    //             // Place asks step by step
    //             for (int i = 0; i < OrderBook.CAPACITY; i++) {
    //                 int priceTick = startPrice + (i * stepSize);
    //                 Order askOrder = new Order(priceTick, 10, "AskUser" + i, Side.ASK);
    //                 book.placeOrder(askOrder);
    //             }

    //             // Place bids to clear all asks
    //             for (int i = 0; i < OrderBook.CAPACITY; i++) {
    //                 int priceTick = startPrice + (i * stepSize);
    //                 Order bidOrder = new Order(priceTick, 10, "BidUser" + i, Side.BID);
    //                 book.placeOrder(bidOrder);
    //             }

    //             // Update start price for next round
    //             startPrice += OrderBook.CAPACITY * stepSize;
    //         }
    //     // });
    // }
}
