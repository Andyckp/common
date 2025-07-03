package com.ac.derivativepricer.adaptor.output;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.nio.ByteBuffer;

import org.agrona.concurrent.UnsafeBuffer;
import static org.slf4j.LoggerFactory.getLogger;

import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_ADJUSTED_GREEK_CAPTURE;
import com.ac.derivativepricer.codec.AdjustedGreekEncoder;
import com.ac.derivativepricer.data.AdjustedGreekEvent;
import com.lmax.disruptor.RingBuffer;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.archive.client.AeronArchive;

public class AdjustedGreekAdaptor extends AbstractAeronOutputAdaptor<AdjustedGreekEvent> {

    private final AdjustedGreekEncoder encoder = new AdjustedGreekEncoder();
    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(AdjustedGreekEncoder.BLOCK_LENGTH);
    private final UnsafeBuffer buffer = new UnsafeBuffer(byteBuffer);

    public AdjustedGreekAdaptor(Aeron aeron, AeronArchive aeronArchive, RingBuffer<AdjustedGreekEvent> rb) {
        super(aeron, aeronArchive, rb, STREAM_ADJUSTED_GREEK_CAPTURE, AdjustedGreekAdaptor.class.getSimpleName(), getLogger(AdjustedGreekAdaptor.class));
    }

    @Override
    protected boolean write(AdjustedGreekEvent e, long sequence, boolean endOfBatch, Publication publication) throws Exception {
        if (e != null) {
            encoder.wrap(buffer, 0);

            for (int i = 0; i < AdjustedGreekEncoder.strategyIdEncodingLength(); i++) {
                encoder.strategyId(i, (byte) e.getStrategyId()[i]);
            }
            for (int i = 0; i < AdjustedGreekEncoder.instrumentIdEncodingLength(); i++) {
                encoder.instrumentId(i, (byte) e.getInstrumentId()[i]);
            }
            for (int i = 0; i < AdjustedGreekEncoder.underlyingIdEncodingLength(); i++) {
                encoder.underlyingId(i, (byte) e.getUnderlyingId()[i]);
            }

            encoder.theo().mantissa(round(e.getTheo() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.underlyingPrice().mantissa(round(e.getUnderlyingPrice() * pow(10, EXPONENT))).exponent((byte) EXPONENT);

            while (publication.offer(buffer, 0, AdjustedGreekEncoder.BLOCK_LENGTH) < 0) {
                idleStrategy.idle();
            }
            return true;
        }
        return false;
    }
}
