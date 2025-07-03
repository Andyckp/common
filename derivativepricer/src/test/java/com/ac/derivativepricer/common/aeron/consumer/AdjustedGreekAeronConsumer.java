package com.ac.derivativepricer.common.aeron.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI_APP;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_ADJUSTED_GREEK_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_ADJUSTED_GREEK_REPLAY;
import com.ac.derivativepricer.adaptor.input.AbstractAeronInputAdaptor;
import com.ac.derivativepricer.codec.AdjustedGreekDecoder;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class AdjustedGreekAeronConsumer extends AbstractAeronInputAdaptor<Object> {

    private static final Logger logger = LoggerFactory.getLogger(AdjustedGreekAeronConsumer.class);
    private final AdjustedGreekDecoder decoder = new AdjustedGreekDecoder();
    private final GreekIdConsumer greekIdConsumer;
    private final ConcurrentMap<String, Map<String, Integer>> count = new ConcurrentHashMap<>();

    public AdjustedGreekAeronConsumer(AeronArchive aeronArchive, GreekIdConsumer greekIdConsumer) {
        super(aeronArchive, null, CHANNEL_URI_APP, STREAM_ADJUSTED_GREEK_CAPTURE, STREAM_ADJUSTED_GREEK_REPLAY, AdjustedGreekAeronConsumer.class.getSimpleName(), logger);
        this.greekIdConsumer = greekIdConsumer;
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, AdjustedGreekDecoder.BLOCK_LENGTH, AdjustedGreekDecoder.SCHEMA_VERSION);
        logger.info("Recv: strategyId={}, instrumentId={}", decoder.strategyId(), decoder.instrumentId());

        count.computeIfAbsent(decoder.strategyId(), k -> new HashMap<>()).computeIfAbsent(decoder.instrumentId(), k -> 0);
        greekIdConsumer.consume(decoder.strategyId(), decoder.underlyingId(), decoder.instrumentId());
    }

    public ConcurrentMap<String, Map<String, Integer>> getCount() {
        return count;
    }
}
