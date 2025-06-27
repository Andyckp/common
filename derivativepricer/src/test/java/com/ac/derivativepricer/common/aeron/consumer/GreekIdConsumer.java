package com.ac.derivativepricer.common.aeron.consumer;

@FunctionalInterface
public interface GreekIdConsumer {
    void consume(String strategyId, String underlyingId, String instrumentId);
}
