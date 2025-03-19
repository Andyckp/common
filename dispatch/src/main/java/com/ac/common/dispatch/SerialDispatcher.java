package com.ac.common.dispatch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static com.ac.common.dispatch.SerialDispatcher.State.NOT_QUEUED;
import static com.ac.common.dispatch.SerialDispatcher.State.QUEUED;
import static com.ac.common.dispatch.SerialDispatcher.State.SLEEPING;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SerialDispatcher {
    private final BlockingQueue<Message> queue;
    private final Pollable pollable;
    private final Config config;
    private final Executor executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final AtomicReference<State> state = new AtomicReference<>(NOT_QUEUED);
    private final MessageListener listener;

    private final Runnable executionRunnable = new Runnable() {
        @Override
        public void run() {
            state.set(State.EXECUTING);
            listener.onMessage(pollable, callBackRunnable);
        }
    };

    private final Runnable callBackRunnable = new Runnable() {
        @Override
        public void run() {
            if (config.waitTimeInMillis > 0) {
                state.set(SLEEPING);
                scheduledExecutor.schedule(postExecutionRunnable, config.waitTimeInMillis, MILLISECONDS);
            } else {
                postExecutionRunnable.run();
            }
        }
    };

    private final Runnable postExecutionRunnable = () -> {
        state.set(NOT_QUEUED);
        tryExecute();
    };

    public SerialDispatcher(Config config, Executor executor, ScheduledExecutorService scheduledExecutor, MessageListener listener) {
        this.config = config;
        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
        this.listener = listener;
        this.queue = new LinkedBlockingQueue<>(config.initialMessageQueueSize);
        this.pollable = queue::poll;
    }

    public void dispatch(Message msg) {
        if (queue.offer(msg)) {
            tryExecute();
        }
    }

    private void tryExecute() {
        if (queue.isEmpty()) {
            return;
        }
        if (!state.compareAndSet(NOT_QUEUED, QUEUED)) {
            return;
        }
        executor.execute(executionRunnable);
    }

    enum State {
        NOT_QUEUED,
        QUEUED,
        EXECUTING,
        SLEEPING,
    }

    public interface Message {
    }

    public interface MessageListener {
        void onMessage(Pollable pollable, Runnable callBackRunnable);
    }

    public interface Pollable {
        Message poll();
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
