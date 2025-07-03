package com.ac.derivativepricer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.adaptor.AeronManager;
import com.ac.derivativepricer.common.aeron.consumer.AdjustedGreekAeronConsumer;
import com.ac.derivativepricer.common.aeron.consumer.GreekIdConsumer;
import com.ac.derivativepricer.common.aeron.consumer.PrimaryGreekAeronConsumer;
import com.ac.derivativepricer.common.aeron.consumer.SecondaryGreekAeronConsumer;
import com.ac.derivativepricer.common.aeron.producer.InstrumentAeronProducer;
import com.ac.derivativepricer.common.aeron.producer.MarketDataAeronOnDemandProducer;
import com.ac.derivativepricer.common.aeron.producer.StrategyAeronProducer;
import com.ac.derivativepricer.common.aeron.producer.VolatilityAeronOnDemandProducer;

public class ApplicationIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationIntegrationTest.class);

    // counters
    private final GreekIdConsumer primaryGreekIdConsumer = mock(GreekIdConsumer.class);
    private final GreekIdConsumer secondaryGreekIdConsumer = mock(GreekIdConsumer.class);
    private final GreekIdConsumer adjustedGreekIdConsumer = mock(GreekIdConsumer.class);
    private ArgumentCaptor<String> primaryStratCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> primaryUlCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> primaryInstrCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> secondaryStratCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> secondaryUlCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> secondaryInstrCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> adjustedStratCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> adjustedUlCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> adjustedInstrCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    public void GIVEN_pricer_started_WHEN_market_data_update_WHEN_volatility_update_THEN_dependent_instruments_of_new_greeks_published() throws InterruptedException {
        // given aeron
        AeronManager aeronManager = new AeronManager();
        aeronManager.start();

        // given test input producer
        StrategyAeronProducer strategyAeronProducer = new StrategyAeronProducer(aeronManager.getAeron(), aeronManager.getAeronArchive());
        strategyAeronProducer.start();

        InstrumentAeronProducer instrumentAeronProducer = new InstrumentAeronProducer(aeronManager.getAeron(), aeronManager.getAeronArchive());
        instrumentAeronProducer.start();

        VolatilityAeronOnDemandProducer volatilityAeronProducer = new VolatilityAeronOnDemandProducer(aeronManager.getAeron(), aeronManager.getAeronArchive());
        volatilityAeronProducer.start();
        volatilityAeronProducer.write2Surfaces();

        MarketDataAeronOnDemandProducer marketDataAeronProducer = new MarketDataAeronOnDemandProducer(aeronManager.getAeron(), aeronManager.getAeronArchive());
        marketDataAeronProducer.start();
        marketDataAeronProducer.write(1);
        marketDataAeronProducer.write(2);

        // given main app
        ApplicationBuilder application = new ApplicationBuilder(aeronManager);
        application.start();
        Thread.sleep(3000); // application start up and publish archive

        // given test output consumer
        PrimaryGreekAeronConsumer primaryGreekAeronConsumer = new PrimaryGreekAeronConsumer(aeronManager.getAeronArchive(), primaryGreekIdConsumer);
        primaryGreekAeronConsumer.start();

        SecondaryGreekAeronConsumer secondaryreekAeronConsumer = new SecondaryGreekAeronConsumer(aeronManager.getAeronArchive(), secondaryGreekIdConsumer);
        secondaryreekAeronConsumer.start();

        AdjustedGreekAeronConsumer adjustedGreekAeronConsumer = new AdjustedGreekAeronConsumer(aeronManager.getAeronArchive(), adjustedGreekIdConsumer);
        adjustedGreekAeronConsumer.start();

        Thread.sleep(2000); // test consumer start up and read all archives

        // when - market data (MD-1, MD-2) for 2 underlyings (UL-1, UL-2) of 2 strategies
        resetCounters();
        marketDataAeronProducer.write(1);
        marketDataAeronProducer.write(2);

        // then 12, 12, 24 results
        // 2 strategies x 2 underlyings x 3 instruments per each underlying = 12 results
        verify(primaryGreekIdConsumer, timeout(2000).times(12)).consume(
                primaryStratCaptor.capture(), primaryUlCaptor.capture(), primaryInstrCaptor.capture());
        verifyNoMoreInteractions(primaryGreekIdConsumer);
        assertResultDistribution(primaryStratCaptor, primaryUlCaptor, primaryInstrCaptor, 3, 3, 3, 3, 6, 6, 2, 2, 2, 2, 2, 2);

        // 2 strategies x 2 underlyings x 3 instruments per each underlying = 12 results
        verify(secondaryGreekIdConsumer, timeout(2000).times(12)).consume(
                secondaryStratCaptor.capture(), secondaryUlCaptor.capture(), secondaryInstrCaptor.capture());
        verifyNoMoreInteractions(secondaryGreekIdConsumer);
        assertResultDistribution(secondaryStratCaptor, secondaryUlCaptor, secondaryInstrCaptor, 3, 3, 3, 3, 6, 6, 2, 2, 2, 2, 2, 2);

        // 12 results triggered by primary greeks + 12 results triggered by market data = 24 fast adjusted results
        verify(adjustedGreekIdConsumer, timeout(2000).times(24)).consume(
                adjustedStratCaptor.capture(), adjustedUlCaptor.capture(), adjustedInstrCaptor.capture());
        verifyNoMoreInteractions(adjustedGreekIdConsumer);
        assertResultDistribution(adjustedStratCaptor, adjustedUlCaptor, adjustedInstrCaptor, 6, 6, 6, 6, 12, 12, 4, 4, 4, 4, 4, 4);

        // when market data (MD-2) for 1 underlying (UL-2) of 2 strategies
        resetCounters();
        marketDataAeronProducer.write(2);

        // then 6, 6, 12 results
        // 2 strategies x 1 underlyings x 3 instruments per each underlying = 6 results
        verify(primaryGreekIdConsumer, timeout(2000).times(6)).consume(
                primaryStratCaptor.capture(), primaryUlCaptor.capture(), primaryInstrCaptor.capture());
        verifyNoMoreInteractions(primaryGreekIdConsumer);
        assertResultDistribution(primaryStratCaptor, primaryUlCaptor, primaryInstrCaptor, 0, 0, 3, 3, 0, 6, 0, 0, 0, 2, 2, 2);

        // 2 strategies x 1 underlyings x 3 instruments per each underlying = 6 results
        verify(secondaryGreekIdConsumer, timeout(2000).times(6)).consume(
                secondaryStratCaptor.capture(), secondaryUlCaptor.capture(), secondaryInstrCaptor.capture());
        verifyNoMoreInteractions(secondaryGreekIdConsumer);
        assertResultDistribution(secondaryStratCaptor, secondaryUlCaptor, secondaryInstrCaptor, 0, 0, 3, 3, 0, 6, 0, 0, 0, 2, 2, 2);

        // 6 results triggered by primary greeks + 6 results triggered by market data = 12 fast adjusted results
        verify(adjustedGreekIdConsumer, timeout(2000).times(12)).consume(
                adjustedStratCaptor.capture(), adjustedUlCaptor.capture(), adjustedInstrCaptor.capture());
        verifyNoMoreInteractions(adjustedGreekIdConsumer);
        assertResultDistribution(adjustedStratCaptor, adjustedUlCaptor, adjustedInstrCaptor, 0, 0, 6, 6, 0, 12, 0, 0, 0, 4, 4, 4);

        // when volatility (Vol-1) for 1 underlying (UL-1) of 2 strategies
        resetCounters();
        volatilityAeronProducer.write1Expiry(1);

        // then 6, 6, 12 results
        // 2 strategies x 1 underlyings x 3 instruments per each underlying = 6 results
        verify(primaryGreekIdConsumer, timeout(2000).times(6)).consume(
                primaryStratCaptor.capture(), primaryUlCaptor.capture(), primaryInstrCaptor.capture());
        verifyNoMoreInteractions(primaryGreekIdConsumer);
        assertResultDistribution(primaryStratCaptor, primaryUlCaptor, primaryInstrCaptor, 3, 3, 0, 0, 6, 0, 2, 2, 2, 0, 0, 0);

        // 2 strategies x 1 underlyings x 3 instruments per each underlying = 6 results
        verify(secondaryGreekIdConsumer, timeout(2000).times(6)).consume(
                secondaryStratCaptor.capture(), secondaryUlCaptor.capture(), secondaryInstrCaptor.capture());
        verifyNoMoreInteractions(secondaryGreekIdConsumer);
        assertResultDistribution(secondaryStratCaptor, secondaryUlCaptor, secondaryInstrCaptor, 3, 3, 0, 0, 6, 0, 2, 2, 2, 0, 0, 0);

        // 6 results triggered by primary greeks = 6 fast adjusted results
        verify(adjustedGreekIdConsumer, timeout(2000).times(6)).consume(
                adjustedStratCaptor.capture(), adjustedUlCaptor.capture(), adjustedInstrCaptor.capture());
        verifyNoMoreInteractions(adjustedGreekIdConsumer);
        assertResultDistribution(adjustedStratCaptor, adjustedUlCaptor, adjustedInstrCaptor, 3, 3, 0, 0, 6, 0, 2, 2, 2, 0, 0, 0);
    }

    private void assertResultDistribution(
            ArgumentCaptor<String> stratCaptor,
            ArgumentCaptor<String> ulCaptor,
            ArgumentCaptor<String> instrCaptor,
            int s_1_1, int s_1_2, int s_2_1, int s_2_2,
            int ul1, int ul2,
            int i_1_1, int i_1_2, int i_1_3, int i_2_1, int i_2_2, int i_2_3) {

        // Strategy counts
        assertEquals(s_1_1, stratCaptor.getAllValues().stream().filter(s -> "S-1-1".equals(s)).count());
        assertEquals(s_1_2, stratCaptor.getAllValues().stream().filter(s -> "S-1-2".equals(s)).count());
        assertEquals(s_2_1, stratCaptor.getAllValues().stream().filter(s -> "S-2-1".equals(s)).count());
        assertEquals(s_2_2, stratCaptor.getAllValues().stream().filter(s -> "S-2-2".equals(s)).count());

        // Underlying counts
        assertEquals(ul1, ulCaptor.getAllValues().stream().filter(ul -> "UL-1".equals(ul)).count());
        assertEquals(ul2, ulCaptor.getAllValues().stream().filter(ul -> "UL-2".equals(ul)).count());

        // Instrument counts
        assertEquals(i_1_1, instrCaptor.getAllValues().stream().filter(i -> "I-1-1".equals(i)).count());
        assertEquals(i_1_2, instrCaptor.getAllValues().stream().filter(i -> "I-1-2".equals(i)).count());
        assertEquals(i_1_3, instrCaptor.getAllValues().stream().filter(i -> "I-1-3".equals(i)).count());
        assertEquals(i_2_1, instrCaptor.getAllValues().stream().filter(i -> "I-2-1".equals(i)).count());
        assertEquals(i_2_2, instrCaptor.getAllValues().stream().filter(i -> "I-2-2".equals(i)).count());
        assertEquals(i_2_3, instrCaptor.getAllValues().stream().filter(i -> "I-2-3".equals(i)).count());
    }

    private void resetCounters() {
        reset(primaryGreekIdConsumer);
        reset(secondaryGreekIdConsumer);
        reset(adjustedGreekIdConsumer);
        primaryStratCaptor = ArgumentCaptor.forClass(String.class);
        primaryUlCaptor = ArgumentCaptor.forClass(String.class);
        primaryInstrCaptor = ArgumentCaptor.forClass(String.class);
        secondaryStratCaptor = ArgumentCaptor.forClass(String.class);
        secondaryUlCaptor = ArgumentCaptor.forClass(String.class);
        secondaryInstrCaptor = ArgumentCaptor.forClass(String.class);
        adjustedStratCaptor = ArgumentCaptor.forClass(String.class);
        adjustedUlCaptor = ArgumentCaptor.forClass(String.class);
        adjustedInstrCaptor = ArgumentCaptor.forClass(String.class);
    }
}
