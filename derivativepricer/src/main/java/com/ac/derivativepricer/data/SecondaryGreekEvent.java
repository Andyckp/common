package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;

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

    public SecondaryGreekEvent set(SecondaryGreekEvent that) {
        setStrategyId(that.getStrategyId());
        setUnderlyingId(that.getUnderlyingId());
        setInstrumentId(that.getInstrumentId());
        setVega(that.getVega());
        setReferenceVolatility(that.getReferenceVolatility());
        setTheta(that.getTheta());
        setRho(that.getRho());
        setVanna(that.getVanna());
        setVolga(that.getVolga());
        return this;
    }

    public char[] getStrategyId() {
        return strategyId;
    }

    public SecondaryGreekEvent setStrategyId(char[] src) {
        arraycopy(src, 0, this.strategyId, 0, Math.min(src.length, STRATEGY_ID_SIZE));
        return this;
    }

    public char[] getUnderlyingId() {
        return underlyingId;
    }

    public SecondaryGreekEvent setUnderlyingId(char[] src) {
        arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
        return this;
    }

    public char[] getInstrumentId() {
        return instrumentId;
    }

    public SecondaryGreekEvent setInstrumentId(char[] src) {
        arraycopy(src, 0, this.instrumentId, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
        return this;
    }

    public double getVega() {
        return vega;
    }

    public SecondaryGreekEvent setVega(double vega) {
        this.vega = vega;
        return this;
    }

    public double getReferenceVolatility() {
        return referenceVolatility;
    }

    public SecondaryGreekEvent setReferenceVolatility(double referenceVolatility) {
        this.referenceVolatility = referenceVolatility;
        return this;
    }

    public double getTheta() {
        return theta;
    }

    public SecondaryGreekEvent setTheta(double theta) {
        this.theta = theta;
        return this;
    }

    public double getRho() {
        return rho;
    }

    public SecondaryGreekEvent setRho(double rho) {
        this.rho = rho;
        return this;
    }

    public double getVanna() {
        return vanna;
    }

    public SecondaryGreekEvent setVanna(double vanna) {
        this.vanna = vanna;
        return this;
    }

    public double getVolga() {
        return volga;
    }

    public SecondaryGreekEvent setVolga(double volga) {
        this.volga = volga;
        return this;
    }
}
