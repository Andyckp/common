package com.ac.common.aeron.tradeandposition;

import com.ac.common.sbe.TradeDecoder;
import com.ac.common.sbe.TradeEncoder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static com.ac.common.aeron.tradeandposition.DisruptorBuilder.buildPositionDisruptor;
import static com.ac.common.aeron.tradeandposition.DisruptorBuilder.buildTradeDisruptor;

public class PositionProcessTest {
    private static final Logger logger = LoggerFactory.getLogger(PositionProcessTest.class);
    private static final int DISRUPTOR_BUFFER_SIZE = 1024;

    private static final String[] tradeIds = {"TRADE123", "TRADE124", "TRADE125", "TRADE126", "TRADE127"};
    private static final String[] instrumentIds = {"AAPL", "GOOG", "MSFT", "AMZN", "TSLA"};
    private static final int[] marketIds = {1, 2, 3, 4, 5};
    private static final String[] portfolioIds = {"PORT123", "PORT124", "PORT125", "PORT126", "PORT127"};
    private static final byte[] sides = {(byte) 'B', (byte) 'S', (byte) 'B', (byte) 'S', (byte) 'B'};

    @Test
    public void test() {
        // GIVEN
        // middleware and threads
        Disruptor<TradeEvent> tradeDisruptor = buildTradeDisruptor(DISRUPTOR_BUFFER_SIZE);
        Disruptor<PositionEvent> positionDisruptor = buildPositionDisruptor(DISRUPTOR_BUFFER_SIZE);

        // processes
        new PositionProcess(positionDisruptor, tradeDisruptor);
        new PositionPublish(positionDisruptor);

        // start
        tradeDisruptor.start();
        positionDisruptor.start();

        // WHEN
        // Sample trade data
        ByteBuffer byteBuffer = ByteBuffer.allocate(TradeEncoder.BLOCK_LENGTH);
        UnsafeBuffer buffer = new UnsafeBuffer(byteBuffer);
        TradeEncoder tradeEncoder = new TradeEncoder();
        TradeDecoder tradeDecoder = new TradeDecoder();

        for (int i = 0; i < 5; i++) {
            tradeEncoder.wrap(buffer, 0)
                .tradeId(tradeIds[i])
                .instrumentId(instrumentIds[i])
                .marketId(marketIds[i])
                .portfolioId(portfolioIds[i])
                .side(sides[i])
                .createTs(System.currentTimeMillis())
                .isDelete((short) 0);
            tradeEncoder.quantity().mantissa(1000*i+i+1).exponent((byte) 2);
            tradeEncoder.price().mantissa(1000*i+i+1).exponent((byte) 2);

            tradeDecoder.wrap(buffer, 0, TradeEncoder.BLOCK_LENGTH, TradeEncoder.SCHEMA_VERSION);
            publishTrade(tradeDisruptor.getRingBuffer(), tradeDecoder);
        }

        // Allow some time for processing to ensure all events are handled
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }

        tradeDisruptor.shutdown();
        positionDisruptor.shutdown();
    }

    private static void publishTrade(RingBuffer<TradeEvent> ringBuffer, TradeDecoder tradeDecoder) {
        long sequence = ringBuffer.next(); // Get the next sequence for publishing
        try {
            TradeEvent event = ringBuffer.get(sequence); // Get the TradeEvent entry
            event.setInstrumentId(tradeDecoder.instrumentId());
            event.setPortfolioId(tradeDecoder.portfolioId());
            event.setQuantity(tradeDecoder.quantity().mantissa() / Math.pow(10, tradeDecoder.quantity().exponent()));
            event.setPrice(tradeDecoder.price().mantissa() / Math.pow(10, tradeDecoder.price().exponent()));
            event.setSide(String.valueOf((char) tradeDecoder.side()));
        } finally {
            ringBuffer.publish(sequence); // Publish the event
        }
    }
}
