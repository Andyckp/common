package com.ac.derivativepricer.data;

public class MarketDataEvent {

    public static final int MARKET_DATA_ID_SIZE = 8;
    private final char[] markDataId = new char[MARKET_DATA_ID_SIZE];
    private double price;

    public char[] getMarkDataId() {
        return markDataId;
    }

    public void setMarkDataId(char[] src) {
        System.arraycopy(src, 0, this.markDataId, 0, Math.min(src.length, MARKET_DATA_ID_SIZE));
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
