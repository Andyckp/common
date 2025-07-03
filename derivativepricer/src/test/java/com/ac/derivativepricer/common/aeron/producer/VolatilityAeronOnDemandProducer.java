package com.ac.derivativepricer.common.aeron.producer;

import static java.nio.ByteBuffer.allocateDirect;
import java.time.LocalDate;
import java.util.Random;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_VOLATILITY_CAPTURE;
import static com.ac.derivativepricer.adaptor.output.AbstractAeronOutputAdaptor.EXPONENT;
import com.ac.derivativepricer.codec.ExpiryVolatilityEncoder;
import static com.ac.derivativepricer.process.LocalDateCodec.encodeToUint16;

import io.aeron.Aeron;
import io.aeron.ExclusivePublication;
import io.aeron.archive.client.AeronArchive;
import static io.aeron.archive.codecs.SourceLocation.LOCAL;

public class VolatilityAeronOnDemandProducer {

    private static final Logger logger = LoggerFactory.getLogger(VolatilityAeronOnDemandProducer.class);

    private final Aeron aeron;
    private final AeronArchive aeronArchive;
    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();

    private final ExpiryVolatilityEncoder encoder = new ExpiryVolatilityEncoder();
    private final UnsafeBuffer buffer = new UnsafeBuffer(allocateDirect(ExpiryVolatilityEncoder.BLOCK_LENGTH));
    private final Random rand = new Random();
    private ExclusivePublication publication;

    public VolatilityAeronOnDemandProducer(Aeron aeron, AeronArchive aeronArchive) {
        this.aeron = aeron;
        this.aeronArchive = aeronArchive;
    }

    public void start() {
        aeronArchive.startRecording(CHANNEL_URI, STREAM_VOLATILITY_CAPTURE, LOCAL);
        publication = aeron.addExclusivePublication(CHANNEL_URI, STREAM_VOLATILITY_CAPTURE);
        while (!publication.isConnected()) {
            idleStrategy.idle();
        }
    }

    public void write2Surfaces() {
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                encoder.wrap(buffer, 0);
                encoder.volatilityId("Vol-" + i);
                encoder.expiry(encodeToUint16(LocalDate.of(2025, 3 + j, 28)));
                encoder.fittingReference().mantissa(25300000).exponent((byte) EXPONENT);
                encoder.parameter1().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter2().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter3().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter4().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter5().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter6().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter7().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter8().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter9().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter10().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter11().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter12().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter13().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter14().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter15().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
                encoder.parameter16().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);

                while (publication.offer(buffer, 0, ExpiryVolatilityEncoder.BLOCK_LENGTH) < 0) {
                    idleStrategy.idle();
                }
                logger.info("Sent volatility id=Vol-{}", i);
            }
        }
    }

    public void write1Expiry(int i) {
        int j = 1;
        encoder.wrap(buffer, 0);
        encoder.volatilityId("Vol-" + i);
        encoder.expiry(encodeToUint16(LocalDate.of(2025, 3 + j, 28)));
        encoder.fittingReference().mantissa(25300000).exponent((byte) EXPONENT);
        encoder.parameter1().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter2().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter3().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter4().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter5().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter6().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter7().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter8().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter9().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter10().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter11().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter12().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter13().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter14().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter15().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);
        encoder.parameter16().mantissa(rand.nextLong(21000000, 23000000)).exponent((byte) EXPONENT);

        while (publication.offer(buffer, 0, ExpiryVolatilityEncoder.BLOCK_LENGTH) < 0) {
            idleStrategy.idle();
        }
        logger.info("Sent volatility id=Vol-{}", i);
    }
}
