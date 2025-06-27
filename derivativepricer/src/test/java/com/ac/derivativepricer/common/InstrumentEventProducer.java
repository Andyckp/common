package com.ac.derivativepricer.common;

import static java.lang.Double.NaN;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.InstrumentEvent;
import static com.ac.derivativepricer.data.InstrumentEvent.INSTRUMENT_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;
import com.ac.derivativepricer.process.EventLoop;
import com.ac.derivativepricer.process.Process;
import static com.ac.derivativepricer.process.Util.padOrTruncate;
import com.lmax.disruptor.RingBuffer;

public class InstrumentEventProducer implements Process {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentEventProducer.class);
    private final EventLoop eventLoop;

    public InstrumentEventProducer(RingBuffer<InstrumentEvent> rb) {
        this.eventLoop = new EventLoop(() -> {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    long seq = rb.next();
                    try {
                        InstrumentEvent event = rb.get(seq);
                        event.setInstrumentId(padOrTruncate("Inst-" + i + 1, INSTRUMENT_ID_SIZE));
                        event.setUnderlyingId(padOrTruncate("UL-" + i + 1, UNDERLYLING_ID_SIZE));
                        event.setInstrumentType(InstrumentEvent.InstrumentType.values()[j % 3]);
                        event.setStrike(j == 0 ? NaN : 25.5d);
                        event.setExpiry(LocalDate.of(2025, 3, 15));
                    } finally {
                        rb.publish(seq);
                    }
                }
            }
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }, "instrument-producer", logger);
    }

    @Override
    public void start() {
        this.eventLoop.start();
    }

    @Override
    public void stop() {
        this.eventLoop.stop();
    }
}
