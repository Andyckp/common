package com.ac.common.dispatch;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.ac.common.dispatch.SerialDispatcher.State.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SerialDispatcher {
    public interface Message {}
    public interface Pollable {
        Message poll();
    }
    public interface MessageListener {
        void onMessage(Pollable pollable);
    }

    public static class Config {
        private final long waitTimeInMillis;

        public Config(long waitTimeInMillis) {
            this.waitTimeInMillis = waitTimeInMillis;
        }
    }

    enum State {
        NOT_QUEUED,
        QUEUED,
        EXECUTING,
        SLEEPING,
    }

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>(1000);
    private final Pollable pollable = () -> queue.poll();

    private final Config config;
    private final Executor executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final AtomicReference<State> state = new AtomicReference<>(NOT_QUEUED);
    private final MessageListener listener;

    public SerialDispatcher(Config config, Executor executor, ScheduledExecutorService scheduledExecutor, MessageListener listener) {
        this.config = config;
        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
        this.listener = listener;
    }

    public void dispatch(Message msg) {
        queue.offer(msg);
        tryExecute();
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

    private final Runnable executionRunnable = new Runnable() {
        @Override
        public void run() {
            state.set(State.EXECUTING);
            listener.onMessage(pollable);

            if (config.waitTimeInMillis > 0) {
                state.set(SLEEPING);
                scheduledExecutor.schedule(tryExecute, config.waitTimeInMillis, MILLISECONDS);
            } else {
                tryExecute.run();
            }
        }
    };

    private final Runnable tryExecute = () -> {
        state.set(NOT_QUEUED);
        tryExecute();
    };
}
