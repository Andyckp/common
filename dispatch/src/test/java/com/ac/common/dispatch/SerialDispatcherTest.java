package com.ac.common.dispatch;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class SerialDispatcherTest {

    private static final Logger logger = LoggerFactory.getLogger(SerialDispatcherTest.class);

    @Test
    public void test() throws InterruptedException {
        SerialDispatcher serialDispatcher = new SerialDispatcher(new SerialDispatcher.Config(10000),
            Executors.newSingleThreadExecutor(),
            Executors.newSingleThreadScheduledExecutor(),
                pollable -> {
                    SerialDispatcher.Message msg;
                    while ((msg = pollable.poll()) != null) {
                        logger.info("msg={}", msg);
                    }
                }
        );

        for (int i = 0; i < 10; i++) {
            serialDispatcher.dispatch(new StringMessage(String.valueOf(i)));
            Thread.sleep(1000);
        }

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
}