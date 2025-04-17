package com.ac.common.aeron.tradeandposition;

import com.ac.common.sbe.TradeEncoder;
import io.aeron.Aeron;
import io.aeron.ExclusivePublication;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.codecs.SourceLocation;
import io.aeron.archive.status.RecordingPos;
import org.agrona.CloseHelper;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.status.CountersReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TradeTestPublisher {
    private static final Logger logger = LoggerFactory.getLogger(TradeTestPublisher.class);

    public static final String CONTROL_REQUEST_CHANNEL = "aeron:udp?endpoint=localhost:8010";
    public static final String CONTROL_RESPONSE_CHANNEL = "aeron:udp?endpoint=localhost:0";
//    private static final String channel = "aeron:ipc";
    private static final String channel = "aeron:udp?endpoint=localhost:9010";
    private static final int streamCapture = 16;

    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    private final AeronArchive aeronArchive;
    private final Aeron aeron;

    private static final String[] tradeIds = {"TRADE123", "TRADE124", "TRADE125", "TRADE126", "TRADE127"};
    private static final String[] instrumentIds = {"AAPL", "GOOG", "MSFT", "AMZN", "TSLA"};
    private static final int[] marketIds = {1, 2, 3, 4, 5};
    private static final String[] portfolioIds = {"PORT123", "PORT124", "PORT125", "PORT126", "PORT127"};
    private static final byte[] sides = {(byte) 'B', (byte) 'S', (byte) 'B', (byte) 'S', (byte) 'B'};

    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(TradeEncoder.BLOCK_LENGTH);
    private final UnsafeBuffer buffer = new UnsafeBuffer(byteBuffer);
    private final TradeEncoder tradeEncoder = new TradeEncoder();

    public TradeTestPublisher() {
        this.aeron = Aeron.connect();
        this.aeronArchive = AeronArchive.connect(
            new AeronArchive.Context()
                .aeron(aeron)
                .controlRequestChannel(CONTROL_REQUEST_CHANNEL)
                .controlResponseChannel(CONTROL_RESPONSE_CHANNEL)
        );
    }

    public void write() {
        aeronArchive.startRecording(channel, streamCapture, SourceLocation.LOCAL);
        try (ExclusivePublication publication = aeron.addExclusivePublication(channel, streamCapture)) {
            while (!publication.isConnected()) {
                idleStrategy.idle();
            }

            int i = 0;
            while (true) {
                tradeEncoder.wrap(buffer, 0)
                    .tradeId(tradeIds[i/5])
                    .instrumentId(instrumentIds[i/5])
                    .marketId(marketIds[i/5])
                    .portfolioId(portfolioIds[i/5])
                    .side(sides[i/5])
                    .createTs(System.currentTimeMillis())
                    .isDelete((short) 0);
                tradeEncoder.quantity().mantissa(1000 * i + i + 1).exponent((byte) 2);
                tradeEncoder.price().mantissa(1000 * i + i + 1).exponent((byte) 2);

                logger.info("Sending {}", tradeEncoder);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                while (publication.offer(buffer, 0, TradeEncoder.BLOCK_LENGTH) < 0) {
                    idleStrategy.idle();
                }
            }

//            final long stopPosition = publication.position();
//            logger.info("stop position={}", stopPosition);
//            final CountersReader countersReader = aeron.countersReader();
//            int counterId = RecordingPos.findCounterIdBySession(countersReader, publication.sessionId());
//            while (CountersReader.NULL_COUNTER_ID == counterId) {
//                idleStrategy.idle();
//                counterId = RecordingPos.findCounterIdBySession(countersReader, publication.sessionId());
//            }
//
//            long counterValue;
//            while ((counterValue = countersReader.getCounterValue(counterId)) < stopPosition) {
//                logger.info("counter value={}, stop position={}", counterValue, stopPosition);
//                idleStrategy.idle();
//            }
        }
    }

    public void cleanUp() {
        CloseHelper.quietClose(aeronArchive);
        CloseHelper.quietClose(aeron);
    }
}