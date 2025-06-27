package com.ac.derivativepricer;

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
import static com.lmax.disruptor.RingBuffer.createSingleProducer;
import com.lmax.disruptor.YieldingWaitStrategy;

public class NonTransportApplication {

    public static final int RING_BUFFER_BUFFER_SIZE = 1024;

    private final RingBuffer<InstrumentEvent> instrumentRb = createSingleProducer(InstrumentEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<StrategyEvent> strategyRb = createSingleProducer(StrategyEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<ExpiryVolatilityEvent> volatilityRb = createSingleProducer(ExpiryVolatilityEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<MarketDataEvent> marketDataRb = createSingleProducer(MarketDataEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<PrimaryGreekEvent> primaryGreekRb = createSingleProducer(PrimaryGreekEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<SecondaryGreekEvent> secondaryGreekRb = createSingleProducer(SecondaryGreekEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final RingBuffer<AdjustedGreekEvent> adjustedGreekRb = createSingleProducer(AdjustedGreekEvent::new, RING_BUFFER_BUFFER_SIZE, new YieldingWaitStrategy());
    private final PrimaryValuationProcess primaryValuationProcess = new PrimaryValuationProcess(instrumentRb, strategyRb, volatilityRb, marketDataRb, primaryGreekRb);
    private final SecondaryValuationProcess SecondaryValuationProcess = new SecondaryValuationProcess(instrumentRb, strategyRb, volatilityRb, marketDataRb, secondaryGreekRb);
    private final AdjustmentProcess adjustmentProcess = new AdjustmentProcess(primaryGreekRb, marketDataRb, adjustedGreekRb);

    public void start() {
        this.adjustmentProcess.start();
        this.primaryValuationProcess.start();
        this.SecondaryValuationProcess.start();
    }
    
    public void stop() {
        this.SecondaryValuationProcess.stop();
        this.primaryValuationProcess.stop();
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
