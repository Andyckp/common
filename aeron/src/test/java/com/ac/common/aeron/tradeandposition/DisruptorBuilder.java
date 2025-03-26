package com.ac.common.aeron.tradeandposition;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorBuilder {
    public static Disruptor<TradeEvent> buildTradeDisruptor(int bufferSize) {
        return new Disruptor<>(
            TradeEvent::new,
            bufferSize,
            r -> {
                return new Thread(r, "Trade");
            },
            ProducerType.SINGLE,
            new BlockingWaitStrategy()
        );
    }

    public static Disruptor<PositionEvent> buildPositionDisruptor(int bufferSize) {
        return new Disruptor<>(
            PositionEvent::new,
            bufferSize,
            r -> {
                return new Thread(r, "Position");
            },
            ProducerType.SINGLE,
            new BlockingWaitStrategy()
        );
    }
}
