package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;

import static com.ac.derivativepricer.data.InstrumentEvent.INSTRUMENT_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.STRATEGY_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;

public class AdjustedGreekEvent {

    private final char[] strategyId = new char[STRATEGY_ID_SIZE];
    private final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];
    private final char[] instrumentId = new char[INSTRUMENT_ID_SIZE];
    private double theo;
    private double underlyingPrice;

    public AdjustedGreekEvent set(AdjustedGreekEvent that) {
        setStrategyId(that.getStrategyId());
        setUnderlyingId(that.getUnderlyingId());
        setInstrumentId(that.getInstrumentId());
        setTheo(that.getTheo());
        setUnderlyingPrice(that.getUnderlyingPrice());
        return this;
    }
    
    public char[] getStrategyId() {
        return strategyId;
    }
    
    public AdjustedGreekEvent setStrategyId(char[] src) {
        arraycopy(src, 0, this.strategyId, 0, Math.min(src.length, STRATEGY_ID_SIZE));
        return this;
    }
    
    public char[] getUnderlyingId() {
        return underlyingId;
    }
    
    public AdjustedGreekEvent setUnderlyingId(char[] src) {
        arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
        return this;
    }
    
    public char[] getInstrumentId() {
        return instrumentId;
    }
    
    public AdjustedGreekEvent setInstrumentId(char[] src) {
        arraycopy(src, 0, this.instrumentId, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
        return this;
    }
    
    public double getTheo() {
        return theo;
    }
    
    public AdjustedGreekEvent setTheo(double theo) {
        this.theo = theo;
        return this;
    }
    
    public double getUnderlyingPrice() {
        return underlyingPrice;
    }
    
    public AdjustedGreekEvent setUnderlyingPrice(double underlyingPrice) {
        this.underlyingPrice = underlyingPrice;
        return this;
    }
}
