package com.ac.derivativepricer.common.aeron.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ac.derivativepricer.adaptor.AeronManager.CHANNEL_URI_APP;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_PRIMARY_GREEK_CAPTURE;
import static com.ac.derivativepricer.adaptor.AeronManager.STREAM_PRIMARY_GREEK_REPLAY;
import com.ac.derivativepricer.adaptor.input.AbstractAeronInputAdaptor;
import com.ac.derivativepricer.codec.PrimaryGreekDecoder;

import io.aeron.archive.client.AeronArchive;
import io.aeron.logbuffer.Header;

public class PrimaryGreekAeronConsumer extends AbstractAeronInputAdaptor<Object> {

    private static final Logger logger = LoggerFactory.getLogger(PrimaryGreekAeronConsumer.class);
    private final PrimaryGreekDecoder decoder = new PrimaryGreekDecoder();
    private final GreekIdConsumer greekIdConsumer;
    private final ConcurrentMap<String, Map<String, Integer>> count = new ConcurrentHashMap<>();

    public PrimaryGreekAeronConsumer(AeronArchive aeronArchive, GreekIdConsumer greekIdConsumer) {
        super(aeronArchive, null, CHANNEL_URI_APP, STREAM_PRIMARY_GREEK_CAPTURE, STREAM_PRIMARY_GREEK_REPLAY, PrimaryGreekAeronConsumer.class.getSimpleName(), logger);
        this.greekIdConsumer = greekIdConsumer;
    }

    @Override
    protected void read(final DirectBuffer buffer, final int offset, final int length, final Header header) {
        decoder.wrap(buffer, offset, PrimaryGreekDecoder.BLOCK_LENGTH, PrimaryGreekDecoder.SCHEMA_VERSION);
        logger.info("Recv: strategyId={}, instrumentId={}", decoder.strategyId(), decoder.instrumentId());

        count.computeIfAbsent(decoder.strategyId(), k -> new HashMap<>()).computeIfAbsent(decoder.instrumentId(), k -> 0);
        greekIdConsumer.consume(decoder.strategyId(), decoder.underlyingId(), decoder.instrumentId());
    }

    public ConcurrentMap<String, Map<String, Integer>> getCount() {
        return count;
    }
}
