package com.ac.common.dispatch2;

import com.ac.common.Message;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static com.ac.common.dispatch2.MessageProcessExecutor2.State.IDLE;
import static com.ac.common.dispatch2.MessageProcessExecutor2.State.PENDING;
import static com.ac.common.dispatch2.MessageProcessExecutor2.State.SLEEPING;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MessageProcessExecutor2 {
    private final Config config;
    private final RingBufferQueue ringBuffer;
    private final Disruptor<BatchMessageEvent> disruptor;
    private final ScheduledExecutorService scheduledExecutor;
    private final BatchMessageProcessor processor;
    private final AtomicReference<State> state = new AtomicReference<>(IDLE);

    private final Runnable executionStage = new Runnable() {
        @Override
        public void run() {
            state.set(State.EXECUTING);
            processor.onMessage(ringBuffer, executionEndCallbackStage);
        }
    };

    private final Runnable executionEndCallbackStage = new Runnable() {
        @Override
        public void run() {
            if (config.waitTimeInMillis > 0) {
                state.set(SLEEPING);
                scheduledExecutor.schedule(nextExecutionTriggerStage, config.waitTimeInMillis, MILLISECONDS);
            } else {
                nextExecutionTriggerStage.run();
            }
        }
    };

    private final Runnable nextExecutionTriggerStage = () -> {
        state.set(IDLE);
        tryExecute();
    };

    public MessageProcessExecutor2(Config config, Disruptor<BatchMessageEvent> disruptor, ScheduledExecutorService scheduledExecutor, BatchMessageProcessor processor) {
        this.config = config;
        this.disruptor = disruptor;
        this.scheduledExecutor = scheduledExecutor;
        this.processor = processor;
        this.ringBuffer = new RingBufferQueue(config.initialMessageQueueSize); 
    }

    public void offer(Message<?, ?> message) {
        ringBuffer.enqueue(message);
        tryExecute();
    }

    private void tryExecute() {
        if (ringBuffer.isEmpty()) {
            return;
        }
        if (!state.compareAndSet(IDLE, PENDING)) {
            return;
        }
        disruptor.publishEvent((event, sequence) -> {
            event.setRunnable(executionStage);
        });
    }

    public enum State {
        IDLE,
        PENDING,
        EXECUTING,
        SLEEPING,
    }

    public static class Config {
        private final int initialMessageQueueSize;
        private final long waitTimeInMillis;

        public Config(int initialMessageQueueSize, long waitTimeInMillis) {
            this.initialMessageQueueSize = initialMessageQueueSize;
            this.waitTimeInMillis = waitTimeInMillis;
        }
    }
}