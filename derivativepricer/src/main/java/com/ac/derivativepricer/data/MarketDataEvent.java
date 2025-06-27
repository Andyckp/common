package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;

public class MarketDataEvent {

    public static final int MARKET_DATA_ID_SIZE = 8;

    private final char[] markDataId = new char[MARKET_DATA_ID_SIZE];
    private double price;

    public MarketDataEvent set(MarketDataEvent that) {
        setMarkDataId(that.getMarkDataId());
        setPrice(that.getPrice());
        return this;
    }
    
    public char[] getMarkDataId() {
        return markDataId;
    }
    
    public MarketDataEvent setMarkDataId(char[] src) {
        arraycopy(src, 0, this.markDataId, 0, Math.min(src.length, MARKET_DATA_ID_SIZE));
        return this;
    }
    
    public double getPrice() {
        return price;
    }
    
    public MarketDataEvent setPrice(double price) {
        this.price = price;
        return this;
    }

    public static char[] copyMarketDataId(char[] src) {
        char[] res = new char[MARKET_DATA_ID_SIZE];
        arraycopy(src, 0, res, 0, Math.min(src.length, MARKET_DATA_ID_SIZE));
        return res;
    }

    // public static String copyMarketDataId(char[] src) {
    //     return valueOf(src, 0, MARKET_DATA_ID_SIZE).intern();
    // }
}
