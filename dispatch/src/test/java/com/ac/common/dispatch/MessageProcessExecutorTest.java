package com.ac.common.dispatch;

import com.ac.common.Message;
import com.ac.common.MessageImpl;
import com.ac.common.dispatch.MessageProcessExecutor.Config;
import com.ac.common.dispatch.MessageProcessExecutor.Processor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class MessageProcessExecutorTest {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessExecutorTest.class);

    interface ProcessingDetails {
        void run(Runnable processEndCallback);
    }

    @Test
    public void GIVEN_two_message_process_executors_and_one_executor_WHEN_process_message_async_THEN_execute_as_expected() throws InterruptedException, ExecutionException {
        // Given
        Executor executor = newSingleThreadScheduledExecutor();
        ScheduledExecutorService scheduledExecutor = newSingleThreadScheduledExecutor();
        innerTest(executor, scheduledExecutor, processEndCallback -> scheduledExecutor.schedule(processEndCallback, 200, MILLISECONDS)); // use timer to mimic heavy 200ms calculation async, i.e. the 1 executor as mere async dispatcher
    }

    @Test
    public void GIVEN_two_message_process_executors_and_two_executors_WHEN_process_message_sync_THEN_execute_as_expected() throws InterruptedException, ExecutionException {
        // Given
        Executor executor = newFixedThreadPool(2);
        ScheduledExecutorService scheduledExecutor = newSingleThreadScheduledExecutor();
        innerTest(executor, scheduledExecutor, processEndCallback -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            processEndCallback.run();
        }); // the 2 executors execute heavy 200 ms calculation
    }

    private static void innerTest(Executor executor, ScheduledExecutorService scheduledExecutor, ProcessingDetails processingDetails) throws InterruptedException, ExecutionException {
        Counter counter1 = mock(Counter.class);
        Processor processor1 = (pollable, processEndCallback) -> {
            Message<?, ?> message;
            while ((message = pollable.poll()) != null) {
                counter1.increment();
                logger.info("message: {}", message);
            }
            processingDetails.run(processEndCallback);

        };
        Counter counter2 = mock(Counter.class);
        Processor processor2 = (pollable, processEndCallback) -> {
            Message<?, ?> message;
            while ((message = pollable.poll()) != null) {
                counter2.increment();
                logger.info("message: {}", message);
            }
            processingDetails.run(processEndCallback);
        };
        MessageProcessExecutor messageProcessExecutor1 = new MessageProcessExecutor(new Config(1000, 500), executor, scheduledExecutor, processor1);
        MessageProcessExecutor messageProcessExecutor2 = new MessageProcessExecutor(new Config(1000, 500), executor, scheduledExecutor, processor2);

        // When
        // Message submitter 1
        CompletableFuture<?> f1 = new CompletableFuture<>();
        newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 100; i++) {
                messageProcessExecutor1.offer(new MessageImpl<>("a"+i, "v"));
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            f1.complete(null);
        });

        // Message submitter 2
        CompletableFuture<?> f2 = new CompletableFuture<>();
        newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 100; i++) {
                messageProcessExecutor2.offer(new MessageImpl<>("b"+i, "v"));
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            f2.complete(null);
        });

        CompletableFuture<?> f3 = allOf(f1, f2);
        f3.get();

        // Then
        verify(counter1, timeout(1000).times(100)).increment();
        verify(counter2, timeout(1000).times(100)).increment();
    }

    interface Counter {
        void increment();
    }
}