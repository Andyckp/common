package com.ac.derivativepricer.adaptor.output;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.nio.ByteBuffer;

import org.agrona.concurrent.UnsafeBuffer;
import static org.slf4j.LoggerFactory.getLogger;

import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_SECONDARY_GREEK_CAPTURE;
import com.ac.derivativepricer.codec.SecondaryGreekEncoder;
import com.ac.derivativepricer.data.SecondaryGreekEvent;
import com.lmax.disruptor.RingBuffer;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.archive.client.AeronArchive;

public class SecondaryGreekAdaptor extends AbstractAeronOutputAdaptor<SecondaryGreekEvent> {

    private final SecondaryGreekEncoder encoder = new SecondaryGreekEncoder();
    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SecondaryGreekEncoder.BLOCK_LENGTH);
    private final UnsafeBuffer buffer = new UnsafeBuffer(byteBuffer);

    public SecondaryGreekAdaptor(Aeron aeron, AeronArchive aeronArchive, RingBuffer<SecondaryGreekEvent> rb) {
        super(aeron, aeronArchive, rb, STREAM_SECONDARY_GREEK_CAPTURE, SecondaryGreekAdaptor.class.getSimpleName(), getLogger(SecondaryGreekAdaptor.class));
    }

    @Override
    protected boolean write(SecondaryGreekEvent e, long sequence, boolean endOfBatch, Publication publication) throws Exception {
        if (e != null) {
            encoder.wrap(buffer, 0);

            for (int i = 0; i < SecondaryGreekEncoder.strategyIdEncodingLength(); i++) {
                encoder.strategyId(i, (byte) e.getStrategyId()[i]);
            }
            for (int i = 0; i < SecondaryGreekEncoder.instrumentIdEncodingLength(); i++) {
                encoder.instrumentId(i, (byte) e.getInstrumentId()[i]);
            }
            for (int i = 0; i < SecondaryGreekEncoder.underlyingIdEncodingLength(); i++) {
                encoder.underlyingId(i, (byte) e.getUnderlyingId()[i]);
            }

            encoder.vega().mantissa(round(e.getVega() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.referenceVolatility().mantissa(round(e.getReferenceVolatility() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.theta().mantissa(round(e.getTheta() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.rho().mantissa(round(e.getRho() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.vanna().mantissa(round(e.getVanna() * pow(10, EXPONENT))).exponent((byte) EXPONENT);
            encoder.volga().mantissa(round(e.getVolga() * pow(10, EXPONENT))).exponent((byte) EXPONENT);

            while (publication.offer(buffer, 0, SecondaryGreekEncoder.BLOCK_LENGTH) < 0) {
                idleStrategy.idle();
            }
            return true;
        }
        return false;
    }
}
