package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;

import static com.ac.derivativepricer.data.InstrumentEvent.INSTRUMENT_ID_SIZE;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.STRATEGY_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;

public class PrimaryGreekEvent {

    private final char[] strategyId = new char[STRATEGY_ID_SIZE];
    private final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];
    private final char[] instrumentId = new char[INSTRUMENT_ID_SIZE];
    private final char[] referenceMarketDataId = new char[MARKET_DATA_ID_SIZE];
    private double theo;
    private double delta;
    private double gamma;
    private double referenceUnderlyingPrice;

    public PrimaryGreekEvent set(PrimaryGreekEvent that) {
        setStrategyId(that.getStrategyId());
        setUnderlyingId(that.getUnderlyingId());
        setInstrumentId(that.getInstrumentId());
        setReferenceMarketDataId(that.getReferenceMarketDataId());
        setTheo(that.getTheo());
        setDelta(that.getDelta());
        setGamma(that.getGamma());
        setReferenceUnderlyingPrice(that.getReferenceUnderlyingPrice());
        return this;
    }
    
    public char[] getStrategyId() {
        return strategyId;
    }
    
    public PrimaryGreekEvent setStrategyId(char[] src) {
        arraycopy(src, 0, this.strategyId, 0, Math.min(src.length, STRATEGY_ID_SIZE));
        return this;
    }
    
    public char[] getUnderlyingId() {
        return underlyingId;
    }
    
    public PrimaryGreekEvent setUnderlyingId(char[] src) {
        arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
        return this;
    }
    
    public char[] getInstrumentId() {
        return instrumentId;
    }

    public PrimaryGreekEvent setInstrumentId(char[] src) {
        arraycopy(src, 0, this.instrumentId, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
        return this;
    }
    
    public char[] getReferenceMarketDataId() {
        return referenceMarketDataId;
    }
    
    public PrimaryGreekEvent setReferenceMarketDataId(char[] src) {
        arraycopy(src, 0, this.referenceMarketDataId, 0, Math.min(src.length, MARKET_DATA_ID_SIZE));
        return this;
    }
    
    public double getTheo() {
        return theo;
    }
    
    public PrimaryGreekEvent setTheo(double theo) {
        this.theo = theo;
        return this;
    }
    
    public double getDelta() {
        return delta;
    }
    
    public PrimaryGreekEvent setDelta(double delta) {
        this.delta = delta;
        return this;
    }
    
    public double getGamma() {
        return gamma;
    }
    
    public PrimaryGreekEvent setGamma(double gamma) {
        this.gamma = gamma;
        return this;
    }
    
    public double getReferenceUnderlyingPrice() {
        return referenceUnderlyingPrice;
    }
    
    public PrimaryGreekEvent setReferenceUnderlyingPrice(double referenceUnderlyingPrice) {
        this.referenceUnderlyingPrice = referenceUnderlyingPrice;
        return this;
    }
}
