package com.ac.derivativepricer;

import org.junit.jupiter.api.Test;

import com.ac.derivativepricer.common.nontransport.producer.InstrumentEventProducer;
import com.ac.derivativepricer.common.nontransport.producer.MarketDataEventProducer;
import com.ac.derivativepricer.common.nontransport.producer.StrategyEventProducer;
import com.ac.derivativepricer.common.nontransport.producer.VolatilityEventProducer;

public class ApplicationNonTransportStartupTest {

    @Test
    public void runAppForSpecifiedTime() throws InterruptedException {
        ProducerConnectedNonTransportApplication app = new ProducerConnectedNonTransportApplication();
        app.start();
        Thread.sleep(10000);
    }

    private static class ProducerConnectedNonTransportApplication {

        private final NonTransportApplication nonTransportApplication = new NonTransportApplication();
        private final StrategyEventProducer strategyEventProducer;
        private final InstrumentEventProducer instrumentEventProducer;
        private final VolatilityEventProducer volatilityEventProducer;
        private final MarketDataEventProducer marketDataEventProducer;

        public ProducerConnectedNonTransportApplication() {
            this.strategyEventProducer = new StrategyEventProducer(nonTransportApplication.getStrategyRb());
            this.instrumentEventProducer = new InstrumentEventProducer(nonTransportApplication.getInstrumentRb());
            this.marketDataEventProducer = new MarketDataEventProducer(nonTransportApplication.getMarketDataRb());
            this.volatilityEventProducer = new VolatilityEventProducer(nonTransportApplication.getVolatilityRb());
        }

        public void start() {
            nonTransportApplication.start();
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
            nonTransportApplication.stop();
        }
    }
}
