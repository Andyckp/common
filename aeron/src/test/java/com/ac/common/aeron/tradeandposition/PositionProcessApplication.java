package com.ac.common.aeron.tradeandposition;

import com.ac.common.sbe.TradeDecoder;
import com.ac.common.sbe.TradeEncoder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.aeron.Aeron;
import io.aeron.ChannelUri;
import io.aeron.Subscription;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.client.RecordingDescriptorConsumer;
import io.aeron.logbuffer.Header;
import org.agrona.CloseHelper;
import org.agrona.DirectBuffer;
import org.agrona.collections.MutableLong;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.common.aeron.tradeandposition.DisruptorBuilder.buildPositionDisruptor;
import static com.ac.common.aeron.tradeandposition.DisruptorBuilder.buildTradeDisruptor;

public class PositionProcessApplication {
    private static final Logger logger = LoggerFactory.getLogger(PositionProcessApplication.class);
    private static final int DISRUPTOR_BUFFER_SIZE = 1024;

    private static final String CONTROL_REQUEST_CHANNEL = "aeron:udp?endpoint=localhost:8010";
    private static final String CONTROL_RESPONSE_CHANNEL = "aeron:udp?endpoint=localhost:0";
//    private static final String channel = "aeron:ipc";
    private static final String channel = "aeron:udp?endpoint=localhost:9010";
    private final int streamCapture = 16;
    private final int streamReplay = 17;

    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    private AeronArchive aeronArchive;
    private Aeron aeron;

    private final TradeDecoder tradeDecoder = new TradeDecoder();
    private Disruptor<TradeEvent> tradeDisruptor = null;
    private Disruptor<PositionEvent> positionDisruptor = null;

    public void read() {
        final long recordingId = findLatestRecording(aeronArchive, channel, streamCapture);
        final long position = AeronArchive.NULL_POSITION;
        final long length = Long.MAX_VALUE;
        final long sessionId = aeronArchive.startReplay(recordingId, position, length, channel, streamReplay);
        final String channelRead = ChannelUri.addSessionId(channel, (int) sessionId);

        final Subscription subscription = aeronArchive.context().aeron().addSubscription(channelRead, streamReplay);
        while (!subscription.isConnected()) {
            idleStrategy.idle();
        }

        for (int i = 0; i < 5; i++) {
            logger.info("Waiting for trade events...");
            final int fragments = subscription.poll(this::archiveReader, 1);
            idleStrategy.idle(fragments);
        }
    }

    public void archiveReader(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        tradeDecoder.wrap(buffer, offset, TradeEncoder.BLOCK_LENGTH, TradeEncoder.SCHEMA_VERSION);
        logger.info("Received {}", tradeDecoder);
        RingBuffer<TradeEvent> ringBuffer = tradeDisruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            TradeEvent event = ringBuffer.get(sequence);
            event.setInstrumentId(tradeDecoder.instrumentId());
            event.setPortfolioId(tradeDecoder.portfolioId());
            event.setQuantity(tradeDecoder.quantity().mantissa() / Math.pow(10, tradeDecoder.quantity().exponent()));
            event.setPrice(tradeDecoder.price().mantissa() / Math.pow(10, tradeDecoder.price().exponent()));
            event.setSide(String.valueOf((char) tradeDecoder.side()));
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    private long findLatestRecording(final AeronArchive archive, final String channel, final int stream) {
        final MutableLong lastRecordingId = new MutableLong();
        final RecordingDescriptorConsumer consumer =
            (controlSessionId, correlationId, recordingId,
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
        }
        return lastRecordingId.get();
    }

    public void setup() {
        this.aeron = Aeron.connect();
        this.aeronArchive = AeronArchive.connect(new AeronArchive.Context()
            .controlRequestChannel(CONTROL_REQUEST_CHANNEL)
            .controlResponseChannel(CONTROL_RESPONSE_CHANNEL)
            .aeron(aeron));

        // GIVEN
        // middleware and threads
        tradeDisruptor = buildTradeDisruptor(DISRUPTOR_BUFFER_SIZE);
        positionDisruptor = buildPositionDisruptor(DISRUPTOR_BUFFER_SIZE);

        // processes
        new PositionProcess(positionDisruptor, tradeDisruptor);
        new PositionPublish(positionDisruptor);

        // start
        tradeDisruptor.start();
        positionDisruptor.start();
    }

    public void cleanUp() {
        if (tradeDisruptor != null) {
            tradeDisruptor.shutdown();
        }
        if (positionDisruptor != null) {
            positionDisruptor.shutdown();
        }

        CloseHelper.quietClose(aeronArchive);
        CloseHelper.quietClose(aeron);
    }
}
