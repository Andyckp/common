package com.ac.derivativepricer.adaptor.output;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.nio.ByteBuffer;

import org.agrona.concurrent.UnsafeBuffer;
import static org.slf4j.LoggerFactory.getLogger;

import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_PRIMARY_GREEK_CAPTURE;
import com.ac.derivativepricer.codec.PrimaryGreekEncoder;
import com.ac.derivativepricer.data.PrimaryGreekEvent;
import com.lmax.disruptor.RingBuffer;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.archive.client.AeronArchive;

public class PrimaryGreekAdaptor extends AbstractAeronOutputAdaptor<PrimaryGreekEvent> {

    private final PrimaryGreekEncoder encoder = new PrimaryGreekEncoder();
    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(PrimaryGreekEncoder.BLOCK_LENGTH);
    private final UnsafeBuffer buffer = new UnsafeBuffer(byteBuffer);

    public PrimaryGreekAdaptor(Aeron aeron, AeronArchive aeronArchive, RingBuffer<PrimaryGreekEvent> rb) {
        super(aeron, aeronArchive, rb, STREAM_PRIMARY_GREEK_CAPTURE, PrimaryGreekAdaptor.class.getSimpleName(), getLogger(PrimaryGreekAdaptor.class));
    }

    @Override
    protected boolean write(PrimaryGreekEvent e, long sequence, boolean endOfBatch, Publication publication) throws Exception {
        if (e != null) {
            encoder.wrap(buffer, 0);
            for (int i = 0; i < PrimaryGreekEncoder.strategyIdEncodingLength(); i++) {
                encoder.strategyId(i, (byte) e.getStrategyId()[i]);
            }
            for (int i = 0; i < PrimaryGreekEncoder.instrumentIdEncodingLength(); i++) {
                encoder.instrumentId(i, (byte) e.getInstrumentId()[i]);
            }
            for (int i = 0; i < PrimaryGreekEncoder.underlyingIdEncodingLength(); i++) {
                encoder.underlyingId(i, (byte) e.getUnderlyingId()[i]);
            }
            for (int i = 0; i < PrimaryGreekEncoder.referenceMarketDataIdEncodingLength(); i++) {
                encoder.referenceMarketDataId(i, (byte) e.getReferenceMarketDataId()[i]);
            }
            encoder.theo().mantissa(round(e.getTheo() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.delta().mantissa(round(e.getDelta() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.gamma().mantissa(round(e.getGamma() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.referenceUnderlyingPrice().mantissa(round(e.getReferenceUnderlyingPrice() * pow(10, EXPONENT))).exponent((byte) EXPONENT);

            while (publication.offer(buffer, 0, PrimaryGreekEncoder.BLOCK_LENGTH) < 0) {
                idleStrategy.idle();
            }
            return true;
        }
        return false;
    }
}
