package com.ac.common.disruptor;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorBasedExecutorBuilder {
    public Disruptor<MessageEvent> build() {
        Disruptor<MessageEvent> disruptor =  new Disruptor<MessageEvent>(MessageEvent::new, 1024, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Disruptor");
            }
        }, ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            event.getProcessor().onMessage(event.getPartitionKey(), event.getMessage());
        });

        return disruptor;
    }
}
