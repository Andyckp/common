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
            RingBuffer<PrimaryGreekEvent> primaryGreekRb,
            int partitionId, 
            int partitionCount) {
        super(instrumentRb, strategyRb, volatilityRb, marketDataRb, primaryGreekRb, partitionId, partitionCount, PrimaryValuationProcess.class.getSimpleName(), logger);
    }

    @Override
    protected void publishRandom(char[] strategyId, char[] instrumentId, char[] underlyingId, char[] volatilityId, char[] marketDataId) {
        greekRb.publishEvent((PrimaryGreekEvent event, long sequence) -> {
            event.setStrategyId(strategyId);
            event.setInstrumentId(instrumentId);
            event.setUnderlyingId(underlyingId);
            event.setReferenceMarketDataId(marketDataId);
            event.setTheo(rand.nextDouble());
            event.setDelta(rand.nextDouble());
            event.setGamma(rand.nextDouble());
            event.setReferenceUnderlyingPrice(rand.nextDouble());
        });
    }
}
