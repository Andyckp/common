package com.ac.derivativepricer.data;

import static com.ac.derivativepricer.data.InstrumentEvent.INSTRUMENT_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.STRATEGY_ID_SIZE;
import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;

public class SecondaryGreekEvent {

    private final char[] strategyId = new char[STRATEGY_ID_SIZE];
    private final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];
    private final char[] instrumentId = new char[INSTRUMENT_ID_SIZE];
    private double vega;
    private double referenceVolatility;
    private double theta;
    private double rho;
    private double vanna;
    private double volga;

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

    public double getVega() {
        return vega;
    }

    public void setVega(double vega) {
        this.vega = vega;
    }

    public double getReferenceVolatility() {
        return referenceVolatility;
    }

    public void setReferenceVolatility(double referenceVolatility) {
        this.referenceVolatility = referenceVolatility;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public double getVanna() {
        return vanna;
    }

    public void setVanna(double vanna) {
        this.vanna = vanna;
    }

    public double getVolga() {
        return volga;
    }

    public void setVolga(double volga) {
        this.volga = volga;
    }
}
