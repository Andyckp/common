package com.ac.derivativepricer.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import com.ac.derivativepricer.data.InstrumentEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.PrimaryGreekEvent;
import com.ac.derivativepricer.data.StrategyEvent;
import com.lmax.disruptor.RingBuffer;

public class PrimaryValuationProcess extends ValuationProcess<PrimaryGreekEvent> {

    public static final Logger logger = LoggerFactory.getLogger(PrimaryValuationProcess.class);

    public PrimaryValuationProcess(
            RingBuffer<InstrumentEvent> instrumentRb,
            RingBuffer<StrategyEvent> strategyRb,
            RingBuffer<ExpiryVolatilityEvent> volatilityRb,
            RingBuffer<MarketDataEvent> marketDataRb, 
            RingBuffer<PrimaryGreekEvent> primaryGreekRb) {
        super(instrumentRb, strategyRb, volatilityRb, marketDataRb, primaryGreekRb, PrimaryValuationProcess.class.getSimpleName(), logger);
    }

    @Override
    protected void publishRandom() {
        greekRb.publishEvent((PrimaryGreekEvent event, long sequence) -> {
            event.setStrategyId("strat 1".toCharArray());
            event.setInstrumentId("inst 1".toCharArray());
            event.setUnderlyingId("ul 1".toCharArray());
            event.setTheo(rand.nextDouble());
            event.setDelta(rand.nextDouble());
            event.setGamma(rand.nextDouble());
            event.setReferenceUnderlyingPrice(rand.nextDouble());
        });
    }
}
