package com.ac.derivativepricer.common.aeron.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI_APP;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_SECONDARY_GREEK_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_SECONDARY_GREEK_REPLAY;
import com.ac.derivativepricer.adaptor.input.AbstractAeronInputAdaptor;
import com.ac.derivativepricer.codec.SecondaryGreekDecoder;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class SecondaryGreekAeronConsumer extends AbstractAeronInputAdaptor<Object> {

    private static final Logger logger = LoggerFactory.getLogger(SecondaryGreekAeronConsumer.class);
    private final SecondaryGreekDecoder decoder = new SecondaryGreekDecoder();
    private final GreekIdConsumer greekIdConsumer;
    private final ConcurrentMap<String, Map<String, Integer>> count = new ConcurrentHashMap<>();

    public SecondaryGreekAeronConsumer(AeronArchive aeronArchive, GreekIdConsumer greekIdConsumer) {
        super(aeronArchive, null, CHANNEL_URI_APP, STREAM_SECONDARY_GREEK_CAPTURE, STREAM_SECONDARY_GREEK_REPLAY, SecondaryGreekAeronConsumer.class.getSimpleName(), logger);
        this.greekIdConsumer = greekIdConsumer;
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, SecondaryGreekDecoder.BLOCK_LENGTH, SecondaryGreekDecoder.SCHEMA_VERSION);
        logger.info("Recv: strategyId={}, instrumentId={}", decoder.strategyId(), decoder.instrumentId());

        count.computeIfAbsent(decoder.strategyId(), k -> new HashMap<>()).computeIfAbsent(decoder.instrumentId(), k -> 0);
        greekIdConsumer.consume(decoder.strategyId(), decoder.underlyingId(), decoder.instrumentId());
    }

    public ConcurrentMap<String, Map<String, Integer>> getCount() {
        return count;
    }
}
