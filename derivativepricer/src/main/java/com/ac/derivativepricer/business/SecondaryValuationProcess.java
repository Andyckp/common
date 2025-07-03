package com.ac.derivativepricer.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import com.ac.derivativepricer.data.InstrumentEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.SecondaryGreekEvent;
import com.ac.derivativepricer.data.StrategyEvent;
import com.lmax.disruptor.RingBuffer;

public class SecondaryValuationProcess extends ValuationProcess<SecondaryGreekEvent> {

    public static final Logger logger = LoggerFactory.getLogger(SecondaryGreekEvent.class);

    public SecondaryValuationProcess(
            RingBuffer<InstrumentEvent> instrumentRb,
            RingBuffer<StrategyEvent> strategyRb,
            RingBuffer<ExpiryVolatilityEvent> volatilityRb,
            RingBuffer<MarketDataEvent> marketDataRb,
            RingBuffer<SecondaryGreekEvent> secondaryGreekRb,
            int partitionId, 
            int partitionCount) {
        super(instrumentRb, strategyRb, volatilityRb, marketDataRb, secondaryGreekRb, partitionId, partitionCount, SecondaryGreekEvent.class.getSimpleName(), logger);
    }

    @Override
    protected void publishRandom(char[] strategyId, char[] instrumentId, char[] underlyingId, char[] volatilityId, char[] marketDataId) {
        greekRb.publishEvent((SecondaryGreekEvent event, long sequence) -> {
            event.setStrategyId(strategyId);
            event.setInstrumentId(instrumentId);
            event.setUnderlyingId(underlyingId);
            event.setVega(rand.nextDouble());
            event.setReferenceVolatility(rand.nextDouble());
            event.setTheta(rand.nextDouble());
            event.setRho(rand.nextDouble());
            event.setVanna(rand.nextDouble());
            event.setVolga(rand.nextDouble());
        });
    }
}
