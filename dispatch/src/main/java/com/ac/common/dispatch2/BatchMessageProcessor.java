package com.ac.common.dispatch2;

import com.lmax.disruptor.RingBuffer;

public interface BatchMessageProcessor {
    void onMessage(RingBufferQueue ringBuffer, Runnable processEndCallback);
}
