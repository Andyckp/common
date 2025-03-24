package com.ac.common.dispatch2;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorBuilder {
    public static Disruptor<BatchMessageEvent> build() {
        Disruptor<BatchMessageEvent> disruptor =  new Disruptor<BatchMessageEvent>(BatchMessageEvent::new, 1024, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Disruptor");
            }
        }, ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            event.getRunnable().run();
        });

        return disruptor;
    }
}
