package com.ac.common.disruptor;

import com.ac.common.Message;

public class MessageEvent {
    private String partitionKey;
    private Processor processor;
    private Message<?, ?> message;

    public MessageEvent() {
    }

    // getter and setter
    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public void setMessage(Message<?, ?> message) {
        this.message = message;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public Message<?, ?> getMessage() {
        return message;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
