package com.ac.common.aeron.tradeandposition;

import com.lmax.disruptor.dsl.Disruptor;
import org.agrona.collections.Object2ObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PositionProcess {
    private static final Logger logger = LoggerFactory.getLogger(PositionProcess.class);

    private final Disruptor<PositionEvent> positionDisruptor;
    private final Map<String, Position> positions = new Object2ObjectHashMap<>();

    public PositionProcess(Disruptor<PositionEvent> positionDisruptor, Disruptor<TradeEvent> tradeDisruptor) {
        this.positionDisruptor = positionDisruptor;
        tradeDisruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            Position position = updatePosition(event);
            publishPositionEvent(position);
            logger.info("Processed trade: Instrument ID={}, Portfolio ID={}, Quantity={}, Price={}, Side={}",
                event.getInstrumentId(), event.getPortfolioId(), event.getQuantity(), event.getPrice(), event.getSide());
        });
    }

    private Position updatePosition(TradeEvent trade) {
        String key = trade.getInstrumentId() + ":" + trade.getPortfolioId();
        Position position = positions.computeIfAbsent(key, k -> new Position(trade.getInstrumentId(), trade.getPortfolioId()));
        position.updatePosition(trade.getQuantity(), trade.getPrice(), trade.getSide());
        return position;
    }

    private void publishPositionEvent(Position position) {
        long sequence = positionDisruptor.getRingBuffer().next();
        try {
            PositionEvent event = positionDisruptor.get(sequence);
            event.setInstrumentId(position.getInstrumentId());
            event.setPortfolioId(position.getPortfolioId());
            event.setQuantity(position.getQuantity());
            event.setNotional(position.getNotional());
        } finally {
            positionDisruptor.getRingBuffer().publish(sequence);
        }
    }
}
