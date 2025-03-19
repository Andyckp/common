package com.ac.common.dispatch;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class SerialDispatcherTest {

    private static final Logger logger = LoggerFactory.getLogger(SerialDispatcherTest.class);

    @Test
    public void test() throws InterruptedException, ExecutionException {

        Executor executor = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        SerialDispatcher serialDispatcher1 = build(executor, scheduledExecutor);
        SerialDispatcher serialDispatcher2 = build(executor, scheduledExecutor);

        CompletableFuture<?> f1 = new CompletableFuture<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 100; i++) {
                serialDispatcher1.dispatch(new StringMessage("a" + String.valueOf(i)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            f1.complete(null);
        });

        CompletableFuture<?> f2 = new CompletableFuture<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 100; i++) {
                serialDispatcher2.dispatch(new StringMessage("b" + String.valueOf(i)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            f2.complete(null);
        });

        CompletableFuture<?> f3 = CompletableFuture.allOf(f1, f2);
        f3.get();
    }

    static class StringMessage implements SerialDispatcher.Message {
        private final String msg;

        StringMessage(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "StringMessage{" +
                "msg='" + msg + '\'' +
                '}';
        }
    }

    SerialDispatcher build(Executor executor, ScheduledExecutorService scheduledExecutor) {
        return new SerialDispatcher(new SerialDispatcher.Config(1000, 10000),
            executor, scheduledExecutor,
            (pollable, callBackRunnable) -> {
                SerialDispatcher.Message msg;
                while ((msg = pollable.poll()) != null) {
                    logger.info("msg={}", msg);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                callBackRunnable.run();
            }
        );
    }
}