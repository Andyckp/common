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
            RingBuffer<SecondaryGreekEvent> secondaryGreekRb) {
        super(instrumentRb, strategyRb, volatilityRb, marketDataRb, secondaryGreekRb, SecondaryGreekEvent.class.getSimpleName(), logger);
    }

    @Override
    protected void publishRandom() {
        greekRb.publishEvent((SecondaryGreekEvent event, long sequence) -> {
            event.setStrategyId("strat 1".toCharArray());
            event.setInstrumentId("inst 1".toCharArray());
            event.setUnderlyingId("ul 1".toCharArray());
            event.setVega(rand.nextDouble());
            event.setReferenceVolatility(rand.nextDouble());
            event.setTheta(rand.nextDouble());
            event.setRho(rand.nextDouble());
            event.setVanna(rand.nextDouble());
            event.setVolga(rand.nextDouble());
        });
    }
}
