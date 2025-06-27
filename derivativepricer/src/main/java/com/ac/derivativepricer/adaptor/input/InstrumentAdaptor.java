package com.ac.derivativepricer.adaptor.input;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_INSTRUMENT_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_INSTRUMENT_REPLAY;
import com.ac.derivativepricer.codec.InstrumentDecoder;
import com.ac.derivativepricer.data.InstrumentEvent;
import static com.ac.derivativepricer.process.LocalDateCodec.decodeFromUint16;
import com.lmax.disruptor.RingBuffer;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class InstrumentAdaptor extends AbstractAeronInputAdaptor<InstrumentEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentAdaptor.class);
    private final InstrumentDecoder decoder = new InstrumentDecoder();

    public InstrumentAdaptor(AeronArchive aeronArchive, RingBuffer<InstrumentEvent> rb) {
        super(aeronArchive, rb, CHANNEL_URI, STREAM_INSTRUMENT_CAPTURE, STREAM_INSTRUMENT_REPLAY, InstrumentAdaptor.class.getSimpleName(), logger);
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, InstrumentDecoder.BLOCK_LENGTH, InstrumentDecoder.SCHEMA_VERSION);
        long sequence = rb.next();
        try {
            InstrumentEvent event = rb.get(sequence);
            for (int i = 0; i < InstrumentDecoder.instrumentIdEncodingLength(); i++) {
                event.getInstrumentId()[i] = (char) decoder.instrumentId(i);
            }
            for (int i = 0; i < InstrumentDecoder.underlyingIdEncodingLength(); i++) {
                event.getUnderlyingId()[i] = (char) decoder.underlyingId(i);
            }
            event.setStrike(decoder.strike().mantissa() / Math.pow(10, decoder.strike().exponent()));
            event.setInstrumentType(com.ac.derivativepricer.data.InstrumentEvent.InstrumentType.values()[decoder.instrumentType().ordinal()]);
            event.setExpiry(decodeFromUint16(decoder.expiry()));
        } finally {
            rb.publish(sequence);
        }
    }
}
