package com.ac.derivativepricer.adaptor.output;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.slf4j.Logger;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI_APP;
import com.ac.derivativepricer.process.StartableProcess;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.RingBuffer;

import io.aeron.Aeron;
import io.aeron.ExclusivePublication;
import io.aeron.Publication;
import io.aeron.archive.client.AeronArchive;
import static io.aeron.archive.codecs.SourceLocation.LOCAL;

public abstract class AbstractAeronOutputAdaptor<T> implements StartableProcess {

    public static final int EXPONENT = 6;

    protected final AeronArchive aeronArchive;
    protected final Aeron aeron;
    protected final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    protected final Thread thread;
    protected volatile boolean running = false;

    protected final int captureStream;
    protected final EventPoller<T> poller;
    protected final Logger logger;

    public AbstractAeronOutputAdaptor(Aeron aeron, AeronArchive aeronArchive, RingBuffer<T> rb, int captureStream, String processName, Logger logger) {
        this.aeron = aeron;
        this.aeronArchive = aeronArchive;
        this.poller = rb.newPoller();
        this.captureStream = captureStream;
        this.logger = logger;
        this.thread = new Thread(this::write, processName);
    }

    private void write() {
        aeronArchive.startRecording(CHANNEL_URI_APP, captureStream, LOCAL);

        try (ExclusivePublication publication = aeron.addExclusivePublication(CHANNEL_URI_APP, captureStream)) {
            while (!publication.isConnected()) {
                idleStrategy.idle(); 
            }

            while (running) {
                try {
                    poller.poll((T event, long sequence, boolean endOfBatch) -> {
                        return write(event, sequence, endOfBatch, publication);
                    });
                    idleStrategy.idle();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    protected abstract boolean write(T event, long sequence, boolean endOfBatch, Publication publication) throws Exception;

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
