package com.ac.common.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.ArrayList;
import java.util.List;

public class DisruptorTest {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorTest.class);

    @Test
    public void testTwoDisruptorsSharingSameThread() throws InterruptedException {
        Disruptor<LongEvent> disruptor1 = new Disruptor<>(LongEvent::new, 1024, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Disruptor 1");
            }
        }, ProducerType.SINGLE, new BlockingWaitStrategy());
        Disruptor<LongEvent> disruptor2 = new Disruptor<>(LongEvent::new, 1024, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Disruptor 2");
            }
        }, ProducerType.SINGLE, new BlockingWaitStrategy());

        List<LongEvent> eventList = new ArrayList<>();
        disruptor1.handleEventsWith((event, sequence, endOfBatch) -> {
            eventList.add(event);
            if (endOfBatch) {
                logger.info("Disruptor 1 events: " + eventList);
                eventList.clear();
            }
        });

        // disruptor1.handleEventsWith((event, sequence, endOfBatch) -> logger.info("Disruptor 1: " + event))
        //       .then((event, sequence, endOfBatch) -> logger.info("Disruptor 1 additional handler: " + event));
        disruptor2.handleEventsWith((event, sequence, endOfBatch) -> logger.info("Disruptor 2: " + event))
              .then((event, sequence, endOfBatch) -> logger.info("Disruptor 2 additional handler: " + event));

        disruptor1.start();
        disruptor2.start();

        disruptor1.publishEvent((event, sequence) -> event.set(1L));
        disruptor2.publishEvent((event, sequence) -> event.set(2L));

        disruptor1.shutdown();
        disruptor2.shutdown();

        Thread.sleep(10000);
    }
}

class LongEvent {
    private long value;

    public void set(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongEvent longEvent = (LongEvent) o;

        return value == longEvent.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

}
