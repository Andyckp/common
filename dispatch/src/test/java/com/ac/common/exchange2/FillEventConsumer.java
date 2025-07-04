package com.ac.common.exchange2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

public class FillEventConsumer implements EventHandler<FillEvent> {
    private static final Logger logger = LoggerFactory.getLogger(FillEventConsumer.class);
    
    @Override
    public void onEvent(FillEvent fill, long sequence, boolean endOfBatch) {
        logger.info("FillEvent received: price={}, volume={}",
            fill.getPrice(), fill.getVolume());
        if (sequence % 1000 == 0) {
            logger.info("FillEvents count={}", sequence);
        }
    }
}
