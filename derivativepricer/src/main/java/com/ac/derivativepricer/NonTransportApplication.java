package com.ac.derivativepricer;

import java.util.ArrayList;
import java.util.List;

import com.ac.derivativepricer.business.AdjustmentProcess;
import com.ac.derivativepricer.business.PrimaryValuationProcess;
import com.ac.derivativepricer.business.SecondaryValuationProcess;
import com.ac.derivativepricer.data.AdjustedGreekEvent;
import com.ac.derivativepricer.data.ExpiryVolatilityEvent;
import com.ac.derivativepricer.data.InstrumentEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.PrimaryGreekEvent;
import com.ac.derivativepricer.data.SecondaryGreekEvent;
import com.ac.derivativepricer.data.StrategyEvent;
import com.lmax.disruptor.RingBuffer;
import static com.lmax.disruptor.RingBuffer.createMultiProducer;
import static com.lmax.disruptor.RingBuffer.createSingleProducer;
import com.lmax.disruptor.YieldingWaitStrategy;

public class NonTransportApplication {

    public static final int RING_BUFFER_BUFFER_SIZE = 1024;
    public static final int NUMBER_OF_VALUATION_PARTITION_ON_UL_ID = 2;

    private final RingBuffer<InstrumentEvent> instrumentRb = createSingleProducer(InstrumentEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<StrategyEvent> strategyRb = createSingleProducer(StrategyEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<ExpiryVolatilityEvent> volatilityRb = createSingleProducer(ExpiryVolatilityEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<MarketDataEvent> marketDataRb = createSingleProducer(MarketDataEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<PrimaryGreekEvent> primaryGreekRb = createMultiProducer(PrimaryGreekEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<SecondaryGreekEvent> secondaryGreekRb = createMultiProducer(SecondaryGreekEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<AdjustedGreekEvent> adjustedGreekRb = createSingleProducer(AdjustedGreekEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final List<PrimaryValuationProcess> primaryValuationProcesses = new ArrayList<>(NUMBER_OF_VALUATION_PARTITION_ON_UL_ID);
    private final List<SecondaryValuationProcess> secondaryValuationProcesses = new ArrayList<>(NUMBER_OF_VALUATION_PARTITION_ON_UL_ID);
    private final AdjustmentProcess adjustmentProcess = new AdjustmentProcess(primaryGreekRb, marketDataRb, adjustedGreekRb);

    public NonTransportApplication() {
        for (int i = 0; i < NUMBER_OF_VALUATION_PARTITION_ON_UL_ID; i++) {
            primaryValuationProcesses.add(new PrimaryValuationProcess(instrumentRb, strategyRb, volatilityRb, marketDataRb, primaryGreekRb, i, NUMBER_OF_VALUATION_PARTITION_ON_UL_ID));
            secondaryValuationProcesses.add(new SecondaryValuationProcess(instrumentRb, strategyRb, volatilityRb, marketDataRb, secondaryGreekRb, i, NUMBER_OF_VALUATION_PARTITION_ON_UL_ID));
        }
    }

    public void start() {
        this.adjustmentProcess.start();
        primaryValuationProcesses.forEach(p -> p.start());
        secondaryValuationProcesses.forEach(p -> p.start());
    }
    
    public void stop() {
        secondaryValuationProcesses.forEach(p -> p.stop());
        primaryValuationProcesses.forEach(p -> p.stop());
        this.adjustmentProcess.stop();
    }
    
    public RingBuffer<InstrumentEvent> getInstrumentRb() {
        return instrumentRb;
    }
    
    public RingBuffer<StrategyEvent> getStrategyRb() {
        return strategyRb;
    }
    
    public RingBuffer<ExpiryVolatilityEvent> getVolatilityRb() {
        return volatilityRb;
    }
    
    public RingBuffer<MarketDataEvent> getMarketDataRb() {
        return marketDataRb;
    }
    
    public RingBuffer<PrimaryGreekEvent> getPrimaryGreekRb() {
        return primaryGreekRb;
    }
    
    public RingBuffer<SecondaryGreekEvent> getSecondaryGreekRb() {
        return secondaryGreekRb;
    }

    public RingBuffer<AdjustedGreekEvent> getAdjustedGreekRb() {
        return adjustedGreekRb;
    }
}
