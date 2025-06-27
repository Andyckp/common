package com.ac.derivativepricer.common.aeron.producer;

import static java.nio.ByteBuffer.allocateDirect;
import java.util.Random;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_MARKET_DATA_CAPTURE;
import static com.ac.derivativepricer.adaptor.output.AbstractAeronOutputAdaptor.EXPONENT;
import com.ac.derivativepricer.codec.MarketDataEncoder;

import io.aeron.Aeron;
import io.aeron.ExclusivePublication;
import io.aeron.archive.client.AeronArchive;
import static io.aeron.archive.codecs.SourceLocation.LOCAL;

public class MarketDataAeronOnDemandProducer {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataAeronOnDemandProducer.class);

    private final Aeron aeron;
    private final AeronArchive aeronArchive;
    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();

    private final MarketDataEncoder encoder = new MarketDataEncoder();
    private final UnsafeBuffer buffer = new UnsafeBuffer(allocateDirect(MarketDataEncoder.BLOCK_LENGTH));
    private final Random rand = new Random();
    private ExclusivePublication publication;

    public MarketDataAeronOnDemandProducer(Aeron aeron, AeronArchive aeronArchive) {
        this.aeron = aeron;
        this.aeronArchive = aeronArchive;
    }

    public void start() {
        aeronArchive.startRecording(CHANNEL_URI, STREAM_MARKET_DATA_CAPTURE, LOCAL);
        publication = aeron.addExclusivePublication(CHANNEL_URI, STREAM_MARKET_DATA_CAPTURE);
        while (!publication.isConnected()) {
            idleStrategy.idle();
        }
    }

    public void write(int i) {
        encoder.wrap(buffer, 0);
        encoder.marketDataId("MD-" + i);
        encoder.price().mantissa((long) (rand.nextDouble(25.0, 26.0) * Math.pow(10, EXPONENT))).exponent((byte) EXPONENT);

        while (publication.offer(buffer, 0, MarketDataEncoder.BLOCK_LENGTH) < 0) {
            idleStrategy.idle();
        }
        logger.info("Sent market data id=MD-{}", i);
    }
}
