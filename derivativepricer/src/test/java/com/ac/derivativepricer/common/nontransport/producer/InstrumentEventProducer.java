package com.ac.derivativepricer.common.nontransport.producer;

import static java.lang.Double.NaN;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.InstrumentEvent;
import static com.ac.derivativepricer.data.InstrumentEvent.INSTRUMENT_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;
import com.ac.derivativepricer.process.StartableProcess;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class InstrumentEventProducer implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentEventProducer.class);
    private final Thread thread;
    private volatile boolean running = false;

    public InstrumentEventProducer(RingBuffer<InstrumentEvent> rb) {
        this.thread = new Thread(() -> {
            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 3; j++) {
                    long seq = rb.next();
                    try {
                        InstrumentEvent event = rb.get(seq);
                        event.setInstrumentId(padOrTruncate("I-" + i + "-" + j, INSTRUMENT_ID_SIZE));
                        event.setUnderlyingId(padOrTruncate("UL-" + i, UNDERLYLING_ID_SIZE));
                        event.setInstrumentType(InstrumentEvent.InstrumentType.values()[j % 3]);
                        event.setStrike(j == 0 ? NaN : 25.5d);
                        event.setExpiry(LocalDate.of(2025, 3, 15));
                    } finally {
                        rb.publish(seq);
                    }
                }
            }
        }, "instrument-producer");
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread.start();
    }

    @Override
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
}
