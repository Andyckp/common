package com.ac.derivativepricer.data;

import static com.ac.derivativepricer.data.ExpiryVolatilityEvent.VOLATILITY_ID_SIZE;
import static com.ac.derivativepricer.data.MarketDataEvent.MARKET_DATA_ID_SIZE;

public class StrategyEvent {

    public static final int STRATEGY_ID_SIZE = 8;
    public static final int UNDERLYLING_ID_SIZE = 8;

    private final char[] strategyId = new char[STRATEGY_ID_SIZE];
    private final char[] underlyingId = new char[16];
    private final char[] volatilityId = new char[VOLATILITY_ID_SIZE];
    private final char[] marketDataId = new char[MARKET_DATA_ID_SIZE];

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

    public char[] getVolatilityId() {
        return volatilityId;
    }

    public void setVolatilityId(char[] src) {
        System.arraycopy(src, 0, this.volatilityId, 0, Math.min(src.length, VOLATILITY_ID_SIZE));
    }

    public char[] getMarketDataId() {
        return marketDataId;
    }

    public void setMarketDataId(char[] src) {
        System.arraycopy(src, 0, this.marketDataId, 0, Math.min(src.length, MARKET_DATA_ID_SIZE));
    }
}
