package com.ac.derivativepricer.common;

import com.ac.derivativepricer.ApplicationInMemory;

public class TestApplication {
    private final ApplicationInMemory applicationInMemory = new ApplicationInMemory();
    private final StrategyEventProducer strategyEventProducer;
    private final InstrumentEventProducer instrumentEventProducer;
    private final VolatilityEventProducer volatilityEventProducer;
    private final MarketDataEventProducer marketDataEventProducer;

    public TestApplication() {
        this.strategyEventProducer = new StrategyEventProducer(applicationInMemory.getStrategyRb());
        this.instrumentEventProducer = new InstrumentEventProducer(applicationInMemory.getInstrumentRb());
        this.marketDataEventProducer = new MarketDataEventProducer(applicationInMemory.getMarketDataRb());
        this.volatilityEventProducer = new VolatilityEventProducer(applicationInMemory.getVolatilityRb());
    }

    public void start() {
        applicationInMemory.start();
        strategyEventProducer.start();
        instrumentEventProducer.start();
        volatilityEventProducer.start();
        marketDataEventProducer.start();
    }

    public void stop() {
        marketDataEventProducer.stop();
        volatilityEventProducer.stop();
        instrumentEventProducer.stop();
        strategyEventProducer.stop();
        applicationInMemory.stop();
    }

    public StrategyEventProducer getStrategyEventProducer() {
        return strategyEventProducer;
    }

    public InstrumentEventProducer getInstrumentEventProducer() {
        return instrumentEventProducer;
    }

    public VolatilityEventProducer getVolatilityEventProducer() {
        return volatilityEventProducer;
    }

    public MarketDataEventProducer getMarketDataEventProducer() {
        return marketDataEventProducer;
    }

    public ApplicationInMemory getApplicationInMemory() {
        return applicationInMemory;
    }
}
