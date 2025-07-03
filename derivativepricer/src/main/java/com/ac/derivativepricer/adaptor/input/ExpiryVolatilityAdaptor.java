package com.ac.derivativepricer.adaptor.input;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_VOLATILITY_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_VOLATILITY_REPLAY;
import com.ac.derivativepricer.codec.ExpiryVolatilityDecoder;
import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import static com.ac.derivativepricer.process.LocalDateCodec.decodeFromUint16;
import com.lmax.disruptor.RingBuffer;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class ExpiryVolatilityAdaptor extends AbstractAeronInputAdaptor<ExpiryVolatilityEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ExpiryVolatilityAdaptor.class);
    private final ExpiryVolatilityDecoder decoder = new ExpiryVolatilityDecoder();

    public ExpiryVolatilityAdaptor(AeronArchive aeronArchive, RingBuffer<ExpiryVolatilityEvent> rb) {
        super(aeronArchive, rb, CHANNEL_URI, STREAM_VOLATILITY_CAPTURE, STREAM_VOLATILITY_REPLAY, ExpiryVolatilityAdaptor.class.getSimpleName(), logger);
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, ExpiryVolatilityDecoder.BLOCK_LENGTH, ExpiryVolatilityDecoder.SCHEMA_VERSION);
        long sequence = rb.next();
        try {
            ExpiryVolatilityEvent event = rb.get(sequence);
            for (int i = 0; i < ExpiryVolatilityDecoder.volatilityIdEncodingLength(); i++) {
                event.getVolatilityId()[i] = (char) decoder.volatilityId(i);
            }
            event.setExpiry(decodeFromUint16(decoder.expiry()));
            event.setFittingReference(decoder.fittingReference().mantissa() / Math.pow(10, decoder.fittingReference().exponent()));
            event.setParameter1(decoder.parameter1().mantissa() / Math.pow(10, decoder.parameter1().exponent()));
            event.setParameter2(decoder.parameter2().mantissa() / Math.pow(10, decoder.parameter2().exponent()));
            event.setParameter3(decoder.parameter3().mantissa() / Math.pow(10, decoder.parameter3().exponent()));
            event.setParameter4(decoder.parameter4().mantissa() / Math.pow(10, decoder.parameter4().exponent()));
            event.setParameter5(decoder.parameter5().mantissa() / Math.pow(10, decoder.parameter5().exponent()));
            event.setParameter6(decoder.parameter6().mantissa() / Math.pow(10, decoder.parameter6().exponent()));
            event.setParameter7(decoder.parameter7().mantissa() / Math.pow(10, decoder.parameter7().exponent()));
            event.setParameter8(decoder.parameter8().mantissa() / Math.pow(10, decoder.parameter8().exponent()));
            event.setParameter9(decoder.parameter9().mantissa() / Math.pow(10, decoder.parameter9().exponent()));
            event.setParameter10(decoder.parameter10().mantissa() / Math.pow(10, decoder.parameter10().exponent()));
            event.setParameter11(decoder.parameter11().mantissa() / Math.pow(10, decoder.parameter11().exponent()));
            event.setParameter12(decoder.parameter12().mantissa() / Math.pow(10, decoder.parameter12().exponent()));
            event.setParameter13(decoder.parameter13().mantissa() / Math.pow(10, decoder.parameter13().exponent()));
            event.setParameter14(decoder.parameter14().mantissa() / Math.pow(10, decoder.parameter14().exponent()));
            event.setParameter15(decoder.parameter15().mantissa() / Math.pow(10, decoder.parameter15().exponent()));
            event.setParameter16(decoder.parameter16().mantissa() / Math.pow(10, decoder.parameter16().exponent()));
        } finally {
            rb.publish(sequence);
        }
    }
}
