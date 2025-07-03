package com.ac.derivativepricer.common.aeron.producer;

import static java.nio.ByteBuffer.allocateDirect;
import java.time.LocalDate;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_INSTRUMENT_CAPTURE;
import static com.ac.derivativepricer.adaptor.output.AbstractAeronOutputAdaptor.EXPONENT;
import com.ac.derivativepricer.codec.InstrumentEncoder;
import com.ac.derivativepricer.codec.InstrumentType;
import static com.ac.derivativepricer.process.LocalDateCodec.encodeToUint16;
import com.ac.derivativepricer.process.StartableProcess;

import io.aeron.Aeron;
import io.aeron.ExclusivePublication;
import io.aeron.archive.client.AeronArchive;
import static io.aeron.archive.codecs.SourceLocation.LOCAL;

public class InstrumentAeronProducer implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentAeronProducer.class);

    private final Aeron aeron;
    private final AeronArchive aeronArchive;
    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    private final Thread thread;
    private volatile boolean running = false;

    private final InstrumentEncoder encoder = new InstrumentEncoder();
    private final UnsafeBuffer buffer = new UnsafeBuffer(allocateDirect(InstrumentEncoder.BLOCK_LENGTH));

    public InstrumentAeronProducer(Aeron aeron, AeronArchive aeronArchive) {
        this.aeron = aeron;
        this.aeronArchive = aeronArchive;
        this.thread = new Thread(this::write, InstrumentAeronProducer.class.getSimpleName());
    }

    private void write() {
        aeronArchive.startRecording(CHANNEL_URI, STREAM_INSTRUMENT_CAPTURE, LOCAL);

        try (ExclusivePublication publication = aeron.addExclusivePublication(CHANNEL_URI, STREAM_INSTRUMENT_CAPTURE)) {
            while (!publication.isConnected()) {
                idleStrategy.idle();
            }

            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 3; j++) {
                    encoder.wrap(buffer, 0);
                    encoder.instrumentId("I-" + i + "-" + j);
                    encoder.underlyingId("UL-" + i);
                    encoder.instrumentType(InstrumentType.values()[j]);
                    encoder.strike().mantissa((long) (Math.pow((j == 0) ? Double.NaN : 25.5, EXPONENT))).exponent((byte) EXPONENT);
                    encoder.expiry(encodeToUint16(LocalDate.of(2025, 3, 15)));

                    while (publication.offer(buffer, 0, InstrumentEncoder.BLOCK_LENGTH) < 0) {
                        idleStrategy.idle();
                    }
                    logger.info("Sent instrument id=Inst-{}-{}", i, j);
                }
            }
        }
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
