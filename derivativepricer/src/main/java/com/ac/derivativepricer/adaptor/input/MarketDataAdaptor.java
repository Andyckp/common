package com.ac.derivativepricer.adaptor.input;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_MARKET_DATA_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_MARKET_DATA_REPLAY;
import com.ac.derivativepricer.codec.MarketDataDecoder;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.lmax.disruptor.RingBuffer;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class MarketDataAdaptor extends AbstractAeronInputAdaptor<MarketDataEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataAdaptor.class);
    private final MarketDataDecoder decoder = new MarketDataDecoder();

    public MarketDataAdaptor(AeronArchive aeronArchive, RingBuffer<MarketDataEvent> rb) {
        super(aeronArchive, rb, CHANNEL_URI, STREAM_MARKET_DATA_CAPTURE, STREAM_MARKET_DATA_REPLAY, MarketDataAdaptor.class.getSimpleName(), logger);
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, MarketDataDecoder.BLOCK_LENGTH, MarketDataDecoder.SCHEMA_VERSION);
        long sequence = rb.next();
        try {
            MarketDataEvent event = rb.get(sequence);
            for (int i = 0; i < MarketDataDecoder.marketDataIdEncodingLength(); i++) {
                event.getMarkDataId()[i] = (char) decoder.marketDataId(i);
            }
            event.setPrice(decoder.price().mantissa() / Math.pow(10, decoder.price().exponent()));
        } finally {
            rb.publish(sequence);
        }
    }
}
