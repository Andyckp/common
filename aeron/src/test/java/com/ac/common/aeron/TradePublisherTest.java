package com.ac.common.aeron;

import org.junit.jupiter.api.Test;

import com.ac.common.aeron.TradePublisher;

public class TradePublisherTest {

    @Test
    public void test() {
        TradePublisher tradePublisher = new TradePublisher();
        tradePublisher.run();
    }
}
