package com.ac.common.disruptor;

import com.ac.common.Message;

public interface Processor {
    void onMessage(String partitionKey, Message<?, ?> message);
}
