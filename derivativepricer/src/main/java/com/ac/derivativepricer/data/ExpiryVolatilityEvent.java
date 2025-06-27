package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;
import java.time.LocalDate;

public class ExpiryVolatilityEvent {

    public static final int VOLATILITY_ID_SIZE = 8;

    private final char[] volatilityId = new char[VOLATILITY_ID_SIZE];
    private LocalDate expiry; // TODO use int
    private double fittingReference;
    private double parameter1;
    private double parameter2;
    private double parameter3;
    private double parameter4;
    private double parameter5;
    private double parameter6;
    private double parameter7;
    private double parameter8;
    private double parameter9;
    private double parameter10;
    private double parameter11;
    private double parameter12;
    private double parameter13;
    private double parameter14;
    private double parameter15;
    private double parameter16;

    public ExpiryVolatilityEvent set(ExpiryVolatilityEvent that) {
        setVolatilityId(that.getVolatilityId());
        setExpiry(that.getExpiry());
        setFittingReference(that.getFittingReference());
        setParameter1(that.getParameter1());
        setParameter2(that.getParameter2());
        setParameter3(that.getParameter3());
        setParameter4(that.getParameter4());
        setParameter5(that.getParameter5());
        setParameter6(that.getParameter6());
        setParameter7(that.getParameter7());
        setParameter8(that.getParameter8());
        setParameter9(that.getParameter9());
        setParameter10(that.getParameter10());
        setParameter11(that.getParameter11());
        setParameter12(that.getParameter12());
        setParameter13(that.getParameter13());
        setParameter14(that.getParameter14());
        setParameter15(that.getParameter15());
        setParameter16(that.getParameter16());
        return this;
    }
    
    public char[] getVolatilityId() {
        return volatilityId;
    }
    
    public ExpiryVolatilityEvent setVolatilityId(char[] src) {
        arraycopy(src, 0, this.volatilityId, 0, Math.min(src.length, VOLATILITY_ID_SIZE));
        return this;
    }
    
    public LocalDate getExpiry() {
        return expiry;
    }
    
    public ExpiryVolatilityEvent setExpiry(LocalDate expiry) {
        this.expiry = expiry;
        return this;
    }
    
    public double getParameter1() {
        return parameter1;
    }
    
    public ExpiryVolatilityEvent setParameter1(double parameter1) {
        this.parameter1 = parameter1;
        return this;
    }
    
    public double getParameter2() {
        return parameter2;
    }
    
    public ExpiryVolatilityEvent setParameter2(double parameter2) {
        this.parameter2 = parameter2;
        return this;
    }
    
    public double getParameter3() {
        return parameter3;
    }
    
    public ExpiryVolatilityEvent setParameter3(double parameter3) {
        this.parameter3 = parameter3;
        return this;
    }
    
    public double getParameter4() {
        return parameter4;
    }
    
    public ExpiryVolatilityEvent setParameter4(double parameter4) {
        this.parameter4 = parameter4;
        return this;
    }
    
    public double getFittingReference() {
        return fittingReference;
    }
    
    public ExpiryVolatilityEvent setFittingReference(double referenceUnderlyingPrice) {
        this.fittingReference = referenceUnderlyingPrice;
        return this;
    }
    
    public double getParameter5() {
        return parameter5;
    }
    
    public ExpiryVolatilityEvent setParameter5(double parameter5) {
        this.parameter5 = parameter5;
        return this;
    }
    
    public double getParameter6() {
        return parameter6;
    }
    
    public ExpiryVolatilityEvent setParameter6(double parameter6) {
        this.parameter6 = parameter6;
        return this;
    }
    
    public double getParameter7() {
        return parameter7;
    }
    
    public ExpiryVolatilityEvent setParameter7(double parameter7) {
        this.parameter7 = parameter7;
        return this;
    }
    
    public double getParameter8() {
        return parameter8;
    }
    
    public ExpiryVolatilityEvent setParameter8(double parameter8) {
        this.parameter8 = parameter8;
        return this;
    }
    
    public double getParameter9() {
        return parameter9;
    }
    
    public ExpiryVolatilityEvent setParameter9(double parameter9) {
        this.parameter9 = parameter9;
        return this;
    }
    
    public double getParameter10() {
        return parameter10;
    }
    
    public ExpiryVolatilityEvent setParameter10(double parameter10) {
        this.parameter10 = parameter10;
        return this;
    }
    
    public double getParameter11() {
        return parameter11;
    }
    
    public ExpiryVolatilityEvent setParameter11(double parameter11) {
        this.parameter11 = parameter11;
        return this;
    }
    
    public double getParameter12() {
        return parameter12;
    }
    
    public ExpiryVolatilityEvent setParameter12(double parameter12) {
        this.parameter12 = parameter12;
        return this;
    }
    
    public double getParameter13() {
        return parameter13;
    }
    
    public ExpiryVolatilityEvent setParameter13(double parameter13) {
        this.parameter13 = parameter13;
        return this;
    }
    
    public double getParameter14() {
        return parameter14;
    }
    
    public ExpiryVolatilityEvent setParameter14(double parameter14) {
        this.parameter14 = parameter14;
        return this;
    }
    
    public double getParameter15() {
        return parameter15;
    }
    
    public ExpiryVolatilityEvent setParameter15(double parameter15) {
        this.parameter15 = parameter15;
        return this;
    }
    
    public double getParameter16() {
        return parameter16;
    }
    
    public ExpiryVolatilityEvent setParameter16(double parameter16) {
        this.parameter16 = parameter16;
        return this;
    }

    public static char[] copyVolatilityId(char[] src) {
        char[] res = new char[VOLATILITY_ID_SIZE];
        arraycopy(src, 0, res, 0, Math.min(src.length, VOLATILITY_ID_SIZE));
        return res;
    }

    // public static String copyVolatilityId(char[] src) {
    //     return valueOf(src, 0, VOLATILITY_ID_SIZE).intern();
    // }
}
