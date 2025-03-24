package com.ac.common.dispatch2;

import com.ac.common.Message;
import com.ac.common.MessageImpl;
import com.ac.common.dispatch.MessageProcessExecutor;
import com.ac.common.dispatch2.MessageProcessExecutor2.Config;
import com.ac.common.dispatch.MessageProcessExecutor.Processor;
import com.lmax.disruptor.dsl.Disruptor;

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

public class MessageProcessExecutor2Test {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessExecutor2Test.class);

    interface ProcessingDetails {
        void run(Runnable processEndCallback);
    }

    // @Test
    // public void GIVEN_two_message_process_executors_and_one_executor_WHEN_process_message_async_THEN_execute_as_expected() throws InterruptedException, ExecutionException {
    //     // Given
    //     Disruptor<BatchMessageEvent> disruptor = DisruptorBuilder.build();
    //     ScheduledExecutorService scheduledExecutor = newSingleThreadScheduledExecutor();
    //     innerTest(disruptor, scheduledExecutor, processEndCallback -> scheduledExecutor.schedule(processEndCallback, 200, MILLISECONDS)); // use timer to mimic heavy 200ms calculation async, i.e. the 1 executor as mere async dispatcher
    // }

    @Test
    public void GIVEN_two_message_process_executors_and_two_executors_WHEN_process_message_sync_THEN_execute_as_expected() throws InterruptedException, ExecutionException {
        // Given
        Disruptor<BatchMessageEvent> disruptor = DisruptorBuilder.build();
        disruptor.start();

        ScheduledExecutorService scheduledExecutor = newSingleThreadScheduledExecutor();
        innerTest(disruptor, scheduledExecutor, processEndCallback -> {
            // try {
            //     Thread.sleep(1);
            // } catch (InterruptedException e) {
            //     throw new RuntimeException(e);
            // }
            processEndCallback.run();
        }); // the 2 executors execute heavy 200 ms calculation
    }

    private static void innerTest(Disruptor<BatchMessageEvent> disruptor, ScheduledExecutorService scheduledExecutor, ProcessingDetails processingDetails) throws InterruptedException, ExecutionException {
        Counter counter1 = mock(Counter.class);
        BatchMessageProcessor processor1 = (ringBuffer, processEndCallback) -> {
            Message<?, ?> message;
            while ((message = ringBuffer.dequeue()) != null) {
                counter1.increment();
                // logger.info("message: {}", message);
            }
            processingDetails.run(processEndCallback);

        };
        Counter counter2 = mock(Counter.class);
        BatchMessageProcessor processor2 = (ringBuffer, processEndCallback) -> {
            Message<?, ?> message;
            while ((message = ringBuffer.dequeue()) != null) {
                counter2.increment();
                // logger.info("message: {}", message);
            }
            processingDetails.run(processEndCallback);
        };
        MessageProcessExecutor2 messageProcessExecutor1 = new MessageProcessExecutor2(new Config(1024, 0), disruptor, scheduledExecutor, processor1);
        MessageProcessExecutor2 messageProcessExecutor2 = new MessageProcessExecutor2(new Config(1024, 0), disruptor, scheduledExecutor, processor2);

        long start = System.currentTimeMillis();

        // When
        // Message submitter 1
        CompletableFuture<?> f1 = new CompletableFuture<>();
        newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 500; i++) {
                messageProcessExecutor1.offer(new MessageImpl<>("a"+i, "v"));
                // try {
                //     sleep(1);
                // } catch (InterruptedException e) {
                //     throw new RuntimeException(e);
                // }
            }
            f1.complete(null);
        });

        // Message submitter 2
        CompletableFuture<?> f2 = new CompletableFuture<>();
        newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 500; i++) {
                messageProcessExecutor2.offer(new MessageImpl<>("b"+i, "v"));
                // try {
                //     sleep(1);
                // } catch (InterruptedException e) {
                //     throw new RuntimeException(e);
                // }
            }
            f2.complete(null);
        });

        CompletableFuture<?> f3 = allOf(f1, f2);
        f3.get();

        // Then
        verify(counter1, timeout(1000).times(500)).increment();
        verify(counter2, timeout(1000).times(500)).increment();

        logger.info("Duration={}ms", System.currentTimeMillis() - start);
    }

    interface Counter {
        void increment();
    }
}