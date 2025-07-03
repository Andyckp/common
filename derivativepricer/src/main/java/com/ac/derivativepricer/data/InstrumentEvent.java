package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;
import java.time.LocalDate;

import static com.ac.derivativepricer.data.StrategyEvent.UNDERLYLING_ID_SIZE;

public class InstrumentEvent {

    public static final int INSTRUMENT_ID_SIZE = 8;

    private final char[] instrumentId = new char[INSTRUMENT_ID_SIZE];
    private final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];

    private double strike;
    private LocalDate expiry; // TODO use int
    private InstrumentType instrumentType;
    // TODO add sequence

    public enum InstrumentType {
        STOCK,
        EUROPEAN_CALL,
        EUROPEAN_PUT,
        AMERICAN_CALL,
        AMERICAN_PUT,
        ASIAN_CALL,
        ASIAN_PUT
    }

    public InstrumentEvent set(InstrumentEvent that) {
        setInstrumentId(that.instrumentId);
        setUnderlyingId(that.underlyingId);
        setStrike(that.strike);
        setExpiry(that.expiry);
        setInstrumentType(that.instrumentType);
        return this;
    }
    
    public char[] getInstrumentId() {
        return instrumentId;
    }
    
    public InstrumentEvent setInstrumentId(char[] src) {
        arraycopy(src, 0, this.instrumentId, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
        return this;
    }
    
    public char[] getUnderlyingId() {
        return underlyingId;
    }
    
    public InstrumentEvent setUnderlyingId(char[] src) {
        arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
        return this;
    }
    
    public double getStrike() {
        return strike;
    }

    public InstrumentEvent setStrike(double strike) {
        this.strike = strike;
        return this;
    }
    
    public LocalDate getExpiry() {
        return expiry;
    }
    
    public InstrumentEvent setExpiry(LocalDate expiry) {
        this.expiry = expiry;
        return this;
    }
    
    public InstrumentType getInstrumentType() {
        return instrumentType;
    }
    
    public InstrumentEvent setInstrumentType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
        return this;
    }

    public static char[] copyInstrumentId(char[] src) {
        char[] res = new char[INSTRUMENT_ID_SIZE];
        arraycopy(src, 0, res, 0, Math.min(src.length, INSTRUMENT_ID_SIZE));
        return res;
    }

    // public static String copyInstrumentId(char[] src) {
    //     return valueOf(src, 0, INSTRUMENT_ID_SIZE).intern();
    // }
}
