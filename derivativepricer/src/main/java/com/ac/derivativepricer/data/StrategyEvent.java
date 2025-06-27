package com.ac.derivativepricer.data;

import static java.lang.System.arraycopy;

import static com.ac.derivativepricer.data.ExpiryVolatilityEvent.VOLATILITY_ID_SIZE;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;

public class StrategyEvent {

    public static final int STRATEGY_ID_SIZE = 8;
    public static final int UNDERLYLING_ID_SIZE = 8;

    protected final char[] strategyId = new char[STRATEGY_ID_SIZE];
    protected final char[] underlyingId = new char[UNDERLYLING_ID_SIZE];
    protected final char[] volatilityId = new char[VOLATILITY_ID_SIZE];
    protected final char[] marketDataId = new char[MARKET_DATA_ID_SIZE];

    public StrategyEvent set(StrategyEvent that) {
        setStrategyId(that.getStrategyId());
        setUnderlyingId(that.getUnderlyingId());
        setVolatilityId(that.getVolatilityId());
        setMarketDataId(that.getMarketDataId());
        return this;
    }

    public char[] getStrategyId() {
        return strategyId;
    }

    public StrategyEvent setStrategyId(char[] src) {
        arraycopy(src, 0, this.strategyId, 0, Math.min(src.length, STRATEGY_ID_SIZE));
        return this;
    }

    public char[] getUnderlyingId() {
        return underlyingId;
    }

    public StrategyEvent setUnderlyingId(char[] src) {
        arraycopy(src, 0, this.underlyingId, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
        return this;
    }

    public char[] getVolatilityId() {
        return volatilityId;
    }
    
    public StrategyEvent setVolatilityId(char[] src) {
        arraycopy(src, 0, this.volatilityId, 0, Math.min(src.length, VOLATILITY_ID_SIZE));
        return this;
    }
    
    public char[] getMarketDataId() {
        return marketDataId;
    }
    
    public StrategyEvent setMarketDataId(char[] src) {
        arraycopy(src, 0, this.marketDataId, 0, Math.min(src.length, MARKET_DATA_ID_SIZE));
        return this;
    }
    
    public static char[] copyStrategyId(char[] src) {
        char[] res = new char[STRATEGY_ID_SIZE];
        arraycopy(src, 0, res, 0, Math.min(src.length, STRATEGY_ID_SIZE));
        return res;
    }
    
    public static char[] copyUnderlyingId(char[] src) {
        char[] res = new char[UNDERLYLING_ID_SIZE];
        arraycopy(src, 0, res, 0, Math.min(src.length, UNDERLYLING_ID_SIZE));
        return res;
    }

    // public static String copyStrategyId(char[] src) {
    //     return valueOf(src, 0, STRATEGY_ID_SIZE).intern();
    // }
    
    // public static String copyUnderlyingId(char[] src) {
    //     return valueOf(src, 0, UNDERLYLING_ID_SIZE).intern();
    // }

    
}