package com.ac.common.dispatch2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.common.Message;
import com.ac.common.MessageImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RingBufferQueueTest {
    private static final Logger logger = LoggerFactory.getLogger(RingBufferQueueTest.class);
    private RingBufferQueue queue;

    @BeforeEach
    void setUp() {
        queue = new RingBufferQueue(4); // Buffer size of 4 for tests
    }

    @Test
    void testEnqueueAndDequeue() {
        logger.info("Running testEnqueueAndDequeue");

        queue.enqueue(new MessageImpl<>("Key1", "Item 1"));
        queue.enqueue(new MessageImpl<>("Key2", "Item 2"));

        // Assert the values are dequeued in FIFO order
        Message<?, ?> message1 = queue.dequeue();
        assertThat(message1.getKey(), is("Key1"));
        assertThat(message1.getValue(), is("Item 1"));

        Message<?, ?> message2 = queue.dequeue();
        assertThat(message2.getKey(), is("Key2"));
        assertThat(message2.getValue(), is("Item 2"));

        // Assert the queue is empty now
        assertThat(queue.dequeue(), is(nullValue()));
        logger.info("testEnqueueAndDequeue passed");
    }

    @Test
    void testBufferWraparound() {
        logger.info("Running testBufferWraparound");

        queue.enqueue(new MessageImpl<>("Key1", "Wrap 1"));
        queue.enqueue(new MessageImpl<>("Key2", "Wrap 2"));

        // Dequeue the first item
        assertThat(queue.dequeue().getValue(), is("Wrap 1"));

        // Add a new item that wraps around
        queue.enqueue(new MessageImpl<>("Key3", "Wrap 3"));

        // Assert the remaining items
        assertThat(queue.dequeue().getValue(), is("Wrap 2"));
        assertThat(queue.dequeue().getValue(), is("Wrap 3"));

        // Queue should now be empty
        assertThat(queue.dequeue(), is(nullValue()));
        logger.info("testBufferWraparound passed");
    }

    @Test
    void testEmptyQueueBehavior() {
        logger.info("Running testEmptyQueueBehavior");

        // Assert dequeue returns null when queue is empty
        assertThat(queue.dequeue(), is(nullValue()));

        // Add and consume items
        queue.enqueue(new MessageImpl<>("KeyA", "Item A"));
        queue.enqueue(new MessageImpl<>("KeyB", "Item B"));

        assertThat(queue.dequeue().getValue(), is("Item A"));
        assertThat(queue.dequeue().getValue(), is("Item B"));

        // Assert queue is empty again
        assertThat(queue.dequeue(), is(nullValue()));
        logger.info("testEmptyQueueBehavior passed");
    }
}
