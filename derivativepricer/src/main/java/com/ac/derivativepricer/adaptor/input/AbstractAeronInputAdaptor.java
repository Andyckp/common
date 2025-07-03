package com.ac.derivativepricer.adaptor.input;

import org.agrona.DirectBuffer;
import org.agrona.collections.MutableLong;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.slf4j.Logger;

import com.ac.derivativepricer.process.StartableProcess;
import com.lmax.disruptor.RingBuffer;

import io.aeron.ChannelUri;
import io.aeron.Subscription;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.client.RecordingDescriptorConsumer;
import io.aeron.logbuffer.Header;

public abstract class AbstractAeronInputAdaptor<T> implements StartableProcess {

    protected final AeronArchive aeronArchive;
    protected final RingBuffer<T> rb;
    protected final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    private volatile boolean running = false;
    private final Thread thread;
    private final Logger logger;
    private final String channel;
    private final int captureStream;
    private final int replayStream;

    public AbstractAeronInputAdaptor(AeronArchive aeronArchive, RingBuffer<T> rb, String channel, int captureStream, int replayStream, String processName, Logger logger) {
        this.aeronArchive = aeronArchive;
        this.rb = rb;
        this.logger = logger;
        this.channel = channel;
        this.captureStream = captureStream;
        this.replayStream = replayStream;
        this.thread = new Thread(this::read, processName);
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

    private void read() {
        final long recordingId = findLatestRecording(aeronArchive, channel, captureStream);
        final long position = AeronArchive.NULL_POSITION;
        final long length = Long.MAX_VALUE;
        final long sessionId = aeronArchive.startReplay(recordingId, position, length, channel, replayStream);
        final String channelRead = ChannelUri.addSessionId(channel, (int) sessionId);
        final Subscription subscription = aeronArchive.context().aeron().addSubscription(channelRead, replayStream);

        while (!subscription.isConnected()) {
            idleStrategy.idle();
        }

        while (running) {
            final int fragments = subscription.poll(this::archiveReader, 1);
            idleStrategy.idle(fragments);
        }
    }

    private void archiveReader(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        if (offset + length > buffer.capacity()) {
            logger.warn("Insufficient data in buffer: offset + length exceeds capacity");
            return;
        }
        read(buffer, offset, length, header);
    }

    protected abstract void read(final DirectBuffer buffer, final int offset, final int length, final Header header);

    private long findLatestRecording(final AeronArchive archive, final String channel, final int stream) {
        final MutableLong lastRecordingId = new MutableLong();
        final RecordingDescriptorConsumer consumer
                = (controlSessionId, correlationId, recordingId,
                        startTimestamp, stopTimestamp, startPosition,
                        stopPosition, initialTermId, segmentFileLength,
                        termBufferLength, mtuLength, sessionId,
                        streamId, strippedChannel, originalChannel,
                        sourceIdentity) -> lastRecordingId.set(recordingId);

        final long fromRecordingId = 0L;
        final int recordCount = 100;

        final int foundCount = archive.listRecordingsForUri(fromRecordingId, recordCount, channel, stream, consumer);
        if (foundCount == 0) {
            throw new IllegalStateException("no recordings found"); 
            // TODO now the app depends on the fact that the archive exists and last recording id is non-zero
        }
        return lastRecordingId.get();
    }
}
