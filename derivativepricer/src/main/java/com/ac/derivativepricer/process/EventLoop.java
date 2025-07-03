package com.ac.derivativepricer.process;

import java.util.function.Consumer;

import org.slf4j.Logger;

import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.EventPoller.PollState;
import static com.lmax.disruptor.EventPoller.PollState.PROCESSING;

public class EventLoop {

    public static final int LOGGING_SAMPLE_SIZE = 100;
    private final Thread thread;
    private volatile boolean running = false;

    public EventLoop(Runnable runnable, String processName, Logger logger) {
        this.thread = new Thread(() -> {
            while (running) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    logger.error("Process error in {}", processName, e);
                }
            }
        }, processName);
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // helper function to simplify callers' poll codes 
    public static <T> PollState poll(EventPoller<T> poller, Consumer<T> consumer, Logger logger, String messageType) {
        try {
            return poller.poll((e, seq, eob) -> {
                if (e != null) {
                    consumer.accept(e);
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
            logger.error("Polling error: messageType={}, message={}", messageType, e.getMessage(), e);
            return PROCESSING;
        }
    }
}
