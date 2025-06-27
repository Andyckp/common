package com.ac.derivativepricer.data;

import java.time.LocalDate;

import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;

public class InstrumentEvent {

    public static final int INSTRUMENT_ID_SIZE = 8;

    private final char[] instrumentId = new char[INSTRUMENT_ID_SIZE];
    private final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];

    private double strike;
    private LocalDate expiry;
    private InstrumentType instrumentType;

    public enum InstrumentType {
        STOCK,
        EUROPEAN_CALL,
        EUROPEAN_PUT,
        AMERICAN_CALL,
        AMERICAN_PUT,
        ASIAN_CALL,
        ASIAN_PUT
    }

    public char[] getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(char[] src) {
        System.arraycopy(src, 0, this.instrumentId, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
    }

    public char[] getUnderlyingId() {
        return underlyingId;
    }

    public void setUnderlyingId(char[] src) {
        System.arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }
}
