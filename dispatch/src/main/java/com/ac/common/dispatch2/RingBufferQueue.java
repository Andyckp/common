package com.ac.common.dispatch2;

import com.ac.common.Message;
import com.lmax.disruptor.RingBuffer;

class QueueEvent {
    private Message<?, ?> message;

    public Message<?, ?> getMessage() {
        return message;
    }

    public void setMessage(Message<?, ?> message) {
        this.message = message;
    }
}

public class RingBufferQueue {
    private final RingBuffer<QueueEvent> ringBuffer;
    private long lastReadSequence = -1; 

    public RingBufferQueue(int bufferSize) {
        this.ringBuffer = RingBuffer.createSingleProducer(QueueEvent::new, bufferSize);
    }

    public void enqueue(Message<?, ?> message) {
        long sequence = ringBuffer.next(); 
        try {
            QueueEvent event = ringBuffer.get(sequence); 
            event.setMessage(message); 
        } finally {
            ringBuffer.publish(sequence); 
        }
    }

    public Message<?, ?> dequeue() {
        long cursor = ringBuffer.getCursor(); 
        if (lastReadSequence < cursor) { 
            lastReadSequence++; 
            QueueEvent event = ringBuffer.get(lastReadSequence); 
            return event.getMessage(); 
        } else {
            return null; 
        }
    }

    public int getBufferSize() {
        return (int) ringBuffer.getBufferSize();
    }

    public boolean isEmpty() {
        return lastReadSequence >= ringBuffer.getCursor();
    }
}
