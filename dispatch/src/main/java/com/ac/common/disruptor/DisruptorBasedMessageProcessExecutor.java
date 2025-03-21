package com.ac.common.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.ac.common.Message;

public class DisruptorBasedMessageProcessExecutor {
    private final Disruptor<MessageEvent> disruptor;
    private final Processor processor;
    private final String partitionKey;

    public DisruptorBasedMessageProcessExecutor(String partitionKey, Processor processor, Disruptor<MessageEvent> disruptor) {
        this.partitionKey = partitionKey;
        this.processor = processor;
        this.disruptor = disruptor;
    }

    public void offer(Message<?, ?> message) {
        this.disruptor.publishEvent((event, sequence) -> {
            event.setPartitionKey(partitionKey);
            event.setMessage(message);
            event.setProcessor(processor);
        });
    }
}
