package com.ac.common.dispatch;

import com.ac.common.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static com.ac.common.dispatch.MessageProcessExecutor.State.IDLE;
import static com.ac.common.dispatch.MessageProcessExecutor.State.PENDING;
import static com.ac.common.dispatch.MessageProcessExecutor.State.SLEEPING;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MessageProcessExecutor {
    private final Config config;
    private final BlockingQueue<Message<?, ?>> messageQueue;
    private final Pollable pollable;
    private final Executor executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final Processor processor;
    private final AtomicReference<State> state = new AtomicReference<>(IDLE);

    private final Runnable executionStage = new Runnable() {
        @Override
        public void run() {
            state.set(State.EXECUTING);
            processor.onMessage(pollable, executionEndCallbackStage);
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

    public MessageProcessExecutor(Config config, Executor executor, ScheduledExecutorService scheduledExecutor, Processor processor) {
        this.config = config;
        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
        this.processor = processor;
        this.messageQueue = new LinkedBlockingQueue<>(config.initialMessageQueueSize);
        this.pollable = messageQueue::poll;
    }

    public void offer(Message<?, ?> message) {
        if (messageQueue.offer(message)) {
            tryExecute();
        }
    }

    private void tryExecute() {
        if (messageQueue.isEmpty()) {
            return;
        }
        if (!state.compareAndSet(IDLE, PENDING)) {
            return;
        }
        executor.execute(executionStage);
    }

    public enum State {
        IDLE,
        PENDING,
        EXECUTING,
        SLEEPING,
    }

    public interface Processor {
        void onMessage(Pollable pollable, Runnable processEndCallback);
    }

    public interface Pollable {
        Message<?, ?> poll();
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
