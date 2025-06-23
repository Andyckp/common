package com.ac.common.exchange2;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class ExchangeTest {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeTest.class);
    private static final int BUFFER_SIZE = 4096;

    @Test
    public void test() throws InterruptedException {
        Disruptor<OrderEvent> orderDisruptor = new Disruptor<>(OrderEvent::new, BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(100, TimeUnit.MILLISECONDS));
        Disruptor<InstrumentEvent> instrumentDisruptor = new Disruptor<>(InstrumentEvent::new, BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(100, TimeUnit.MILLISECONDS));
        Disruptor<FillEvent> fillDisruptor = new Disruptor<>(FillEvent::new, BUFFER_SIZE, (Runnable r) -> new Thread(r, "fill-event-disruptor"), ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(100, TimeUnit.MILLISECONDS));
        
        RingBuffer<OrderEvent> orderRingBuffer = orderDisruptor.getRingBuffer();
        RingBuffer<InstrumentEvent> instrumentRingBuffer = instrumentDisruptor.getRingBuffer();
        RingBuffer<FillEvent> fillRingBuffer = fillDisruptor.getRingBuffer();

        OrderInstrumentProcessor orderInstrumentProcessor = new OrderInstrumentProcessor(orderRingBuffer, instrumentRingBuffer, fillRingBuffer);

        FillEventConsumer fillEventConsumer = new FillEventConsumer();
        fillDisruptor.handleEventsWith(fillEventConsumer);
        
        OrderEventProducer orderEventProducer = new OrderEventProducer(orderRingBuffer);
        InstrumentEventProducer instrumentEventProducer = new InstrumentEventProducer(instrumentRingBuffer);

        orderDisruptor.start();
        instrumentDisruptor.start();
        fillDisruptor.start();

        orderInstrumentProcessor.start();
        orderEventProducer.start();
        // instrumentEventProducer.start();

        Thread.sleep(10000); 
        // stats: 10s 66000000 fills, 33000000 orders, 33000000 instruments, if dummy order instrument processor is used

        orderInstrumentProcessor.print();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            orderEventProducer.stop();
            instrumentEventProducer.stop();
            orderInstrumentProcessor.print();
            orderInstrumentProcessor.stop();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            orderDisruptor.shutdown();
            instrumentDisruptor.shutdown();
            fillDisruptor.shutdown();
            
        }));
    }
}
