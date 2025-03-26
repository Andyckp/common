package com.ac.common.aeron.tradeandposition;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ac.common.sbe.PositionEncoder;
import org.agrona.concurrent.UnsafeBuffer;

public class PositionPublish implements EventHandler<PositionEvent> {
    private static final Logger logger = LoggerFactory.getLogger(PositionPublish.class);
    private final PositionEncoder positionEncoder = new PositionEncoder();
    private final UnsafeBuffer buffer = new UnsafeBuffer(new byte[1024]);

    public PositionPublish(Disruptor<PositionEvent> positionDisruptor) {
        positionDisruptor.handleEventsWith(this);
    }

    @Override
    public void onEvent(PositionEvent event, long sequence, boolean endOfBatch) {
        logger.info("Received position event: {}", event);
        positionEncoder.wrap(buffer, 0)
            .instrumentId(event.getInstrumentId())
            .portfolioId(event.getPortfolioId())
            .createTs(System.currentTimeMillis());
        positionEncoder.quantity().mantissa((long) (event.getQuantity() * Math.pow(10, 2))).exponent((byte) 2);
        positionEncoder.notional().mantissa((long) (event.getNotional() * Math.pow(10, 2))).exponent((byte) 2);
        logger.info("Encoded position: {}", buffer.byteArray());
    }
}
