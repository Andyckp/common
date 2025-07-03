package com.ac.derivativepricer.adaptor.input;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_STRATEGY_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_STRATEGY_REPLAY;
import com.ac.derivativepricer.codec.StrategyDecoder;
import com.ac.derivativepricer.data.StrategyEvent;
import com.lmax.disruptor.RingBuffer;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class StrategyAdaptor extends AbstractAeronInputAdaptor<StrategyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StrategyAdaptor.class);
    private final StrategyDecoder decoder = new StrategyDecoder();

    public StrategyAdaptor(AeronArchive aeronArchive, RingBuffer<StrategyEvent> rb) {
        super(aeronArchive, rb, CHANNEL_URI, STREAM_STRATEGY_CAPTURE, STREAM_STRATEGY_REPLAY, StrategyAdaptor.class.getSimpleName(), logger);
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, StrategyDecoder.BLOCK_LENGTH, StrategyDecoder.SCHEMA_VERSION);
        long sequence = rb.next();
        try {
            StrategyEvent event = rb.get(sequence);
            for (int i = 0; i < StrategyDecoder.strategyIdEncodingLength(); i++) {
                event.getStrategyId()[i] = (char) decoder.strategyId(i);
            }
            for (int i = 0; i < StrategyDecoder.underlyingIdEncodingLength(); i++) {
                event.getUnderlyingId()[i] = (char) decoder.underlyingId(i);
            }
            for (int i = 0; i < StrategyDecoder.volatilityIdEncodingLength(); i++) {
                event.getVolatilityId()[i] = (char) decoder.volatilityId(i);
            }
            for (int i = 0; i < StrategyDecoder.marketDataIdEncodingLength(); i++) {
                event.getMarketDataId()[i] = (char) decoder.marketDataId(i);
            }
        } finally {
            rb.publish(sequence);
        }
    }
}
