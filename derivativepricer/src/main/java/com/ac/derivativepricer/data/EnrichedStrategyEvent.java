package com.ac.derivativepricer.data;

import com.ac.derivativepricer.process.CharArrayKey;

public class EnrichedStrategyEvent extends StrategyEvent{
    protected CharArrayKey underlyingKey;
    protected CharArrayKey volatilityKey;
    protected CharArrayKey marketDataKey; 

    public EnrichedStrategyEvent set(StrategyEvent that, CharArrayKey underlyingKey, CharArrayKey volatilityKey, CharArrayKey marketDataKey) {
        setStrategyId(that.getStrategyId());
        setUnderlyingId(that.getUnderlyingId());
        setVolatilityId(that.getVolatilityId());
        setMarketDataId(that.getMarketDataId());
        this.underlyingKey = underlyingKey;
        this.volatilityKey = volatilityKey;
        this.marketDataKey = marketDataKey;

        return this;
    }

    public CharArrayKey getUnderlyingKey() {
        return underlyingKey;
    }

    public CharArrayKey getVolatilityKey() {
        return volatilityKey;
    }

    public CharArrayKey getMarketDataKey() {
        return marketDataKey;
    }
}