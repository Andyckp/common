package com.ac.derivativepricer.data;

import java.time.LocalDate;

public class ExpiryVolatilityEvent {

    public static final int VOLATILITY_ID_SIZE = 8;

    private final char[] volatilityId = new char[VOLATILITY_ID_SIZE];
    private LocalDate expiry;
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

    public char[] getVolatilityId() {
        return volatilityId;
    }

    public void setVolatilityId(char[] src) {
        System.arraycopy(src, 0, this.volatilityId, 0, Math.min(src.length, VOLATILITY_ID_SIZE));
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public double getParameter1() {
        return parameter1;
    }

    public void setParameter1(double parameter1) {
        this.parameter1 = parameter1;
    }

    public double getParameter2() {
        return parameter2;
    }

    public void setParameter2(double parameter2) {
        this.parameter2 = parameter2;
    }

    public double getParameter3() {
        return parameter3;
    }

    public void setParameter3(double parameter3) {
        this.parameter3 = parameter3;
    }

    public double getParameter4() {
        return parameter4;
    }

    public void setParameter4(double parameter4) {
        this.parameter4 = parameter4;
    }

    public double getFittingReference() {
        return fittingReference;
    }

    public void setFittingReference(double referenceUnderlyingPrice) {
        this.fittingReference = referenceUnderlyingPrice;
    }

    public double getParameter5() {
        return parameter5;
    }

    public void setParameter5(double parameter5) {
        this.parameter5 = parameter5;
    }

    public double getParameter6() {
        return parameter6;
    }

    public void setParameter6(double parameter6) {
        this.parameter6 = parameter6;
    }

    public double getParameter7() {
        return parameter7;
    }

    public void setParameter7(double parameter7) {
        this.parameter7 = parameter7;
    }

    public double getParameter8() {
        return parameter8;
    }

    public void setParameter8(double parameter8) {
        this.parameter8 = parameter8;
    }

    public double getParameter9() {
        return parameter9;
    }

    public void setParameter9(double parameter9) {
        this.parameter9 = parameter9;
    }

    public double getParameter10() {
        return parameter10;
    }

    public void setParameter10(double parameter10) {
        this.parameter10 = parameter10;
    }

    public double getParameter11() {
        return parameter11;
    }

    public void setParameter11(double parameter11) {
        this.parameter11 = parameter11;
    }

    public double getParameter12() {
        return parameter12;
    }

    public void setParameter12(double parameter12) {
        this.parameter12 = parameter12;
    }

    public double getParameter13() {
        return parameter13;
    }

    public void setParameter13(double parameter13) {
        this.parameter13 = parameter13;
    }

    public double getParameter14() {
        return parameter14;
    }

    public void setParameter14(double parameter14) {
        this.parameter14 = parameter14;
    }

    public double getParameter15() {
        return parameter15;
    }

    public void setParameter15(double parameter15) {
        this.parameter15 = parameter15;
    }

    public double getParameter16() {
        return parameter16;
    }

    public void setParameter16(double parameter16) {
        this.parameter16 = parameter16;
    }
}
