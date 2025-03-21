package com.ac.common.disruptor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.common.Message;
import com.ac.common.MessageImpl;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorBasedMessageProcessExecutorTest {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorBasedMessageProcessExecutorTest.class);

    @Test
    public void test() throws InterruptedException {
        Disruptor<MessageEvent> disruptor = new DisruptorBasedExecutorBuilder().build();
        disruptor.start();

        DisruptorBasedMessageProcessExecutor disruptorBasedMessageProcessExecutor = new DisruptorBasedMessageProcessExecutor("p1", new Processor() {
            @Override
            public void onMessage(String partitionKey, Message<?, ?> message) {
                logger.info("partitionKey = {}", partitionKey);
                logger.info("message = {}", message);
            }
        }, disruptor);
        DisruptorBasedMessageProcessExecutor disruptorBasedMessageProcessExecutor2 = new DisruptorBasedMessageProcessExecutor("p2", new Processor() {
            @Override
            public void onMessage(String partitionKey, Message<?, ?> message) {
                logger.info("partitionKey = {}", partitionKey);
                logger.info("message = {}", message);
            }
        }, disruptor);

        
        disruptorBasedMessageProcessExecutor.offer(new MessageImpl<>("k1", "v"));
        disruptorBasedMessageProcessExecutor2.offer(new MessageImpl<>("k1", "v"));

        Thread.sleep(1000);
    }
}
