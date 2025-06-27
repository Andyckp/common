package com.ac.derivativepricer.common.aeron.producer;

import static java.nio.ByteBuffer.allocateDirect;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_STRATEGY_CAPTURE;
import com.ac.derivativepricer.codec.StrategyEncoder;
import com.ac.derivativepricer.process.StartableProcess;

import io.aeron.Aeron;
import io.aeron.ExclusivePublication;
import io.aeron.archive.client.AeronArchive;
import static io.aeron.archive.codecs.SourceLocation.LOCAL;

public class StrategyAeronProducer implements StartableProcess {

    private static final Logger logger = LoggerFactory.getLogger(StrategyAeronProducer.class);

    private final AeronArchive aeronArchive;
    private final Aeron aeron;
    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    private final Thread thread;
    private volatile boolean running = false;

    private final StrategyEncoder encoder = new StrategyEncoder();
    private final UnsafeBuffer buffer = new UnsafeBuffer(allocateDirect(StrategyEncoder.BLOCK_LENGTH));

    public StrategyAeronProducer(Aeron aeron, AeronArchive aeronArchive) {
        this.aeron = aeron;
        this.aeronArchive = aeronArchive;
        this.thread = new Thread(this::write, StrategyAeronProducer.class.getSimpleName());
    }

    private void write() {
        aeronArchive.startRecording(CHANNEL_URI, STREAM_STRATEGY_CAPTURE, LOCAL);

        try (ExclusivePublication publication = aeron.addExclusivePublication(CHANNEL_URI, STREAM_STRATEGY_CAPTURE)) {
            while (!publication.isConnected()) {
                idleStrategy.idle();
            }

            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 2; j++) {
                    encoder.wrap(buffer, 0)
                            .strategyId("S-" + j + "-" + i)
                            .underlyingId("UL-" + j)
                            .volatilityId("Vol-" + j)
                            .marketDataId("MD-" + j);

                    while (publication.offer(buffer, 0, StrategyEncoder.BLOCK_LENGTH) < 0) {
                        idleStrategy.idle();
                    }
                    logger.info("Sending id=S-{}-{}", i, j);
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
