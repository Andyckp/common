package com.ac.derivativepricer.data;

import static com.ac.derivativepricer.data.InstrumentEvent.INSTRUMENT_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.STRATEGY_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;

public class PrimaryGreekEvent {

    private final char[] strategyId = new char[STRATEGY_ID_SIZE];
    private final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];
    private final char[] instrumentId = new char[INSTRUMENT_ID_SIZE];
    private double theo;
    private double delta;
    private double gamma;
    private double referenceUnderlyingPrice;

    public char[] getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(char[] src) {
        System.arraycopy(src, 0, this.strategyId, 0, Math.min(src.length, STRATEGY_ID_SIZE));
    }

    public char[] getUnderlyingId() {
        return underlyingId;
    }

    public void setUnderlyingId(char[] src) {
        System.arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
    }

    public char[] getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(char[] src) {
        System.arraycopy(src, 0, this.instrumentId, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
    }

    public double getTheo() {
        return theo;
    }

    public void setTheo(double theo) {
        this.theo = theo;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getReferenceUnderlyingPrice() {
        return referenceUnderlyingPrice;
    }

    public void setReferenceUnderlyingPrice(double referenceUnderlyingPrice) {
        this.referenceUnderlyingPrice = referenceUnderlyingPrice;
    }
}
