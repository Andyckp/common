package com.ac.derivativepricer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.adaptor.AeronManager;
import com.ac.derivativepricer.adaptor.input.ExpiryVolatilityAdaptor;
import com.ac.derivativepricer.adaptor.input.InstrumentAdaptor;
import com.ac.derivativepricer.adaptor.input.MarketDataAdaptor;
import com.ac.derivativepricer.adaptor.input.StrategyAdaptor;
import com.ac.derivativepricer.adaptor.output.AdjustedGreekAdaptor;
import com.ac.derivativepricer.adaptor.output.PrimaryGreekAdaptor;
import com.ac.derivativepricer.adaptor.output.SecondaryGreekAdaptor;

import io.aeron.Aeron;
import io.aeron.archive.client.AeronArchive;

public class ApplicationBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationBuilder.class);

    public ApplicationBuilder(AeronManager aeronManager) {
        this.aeronManager = aeronManager;
    }

    private final NonTransportApplication nonTransportApplication = new NonTransportApplication();

    private StrategyAdaptor strategyAdaptor;
    private InstrumentAdaptor instrumentAdaptor;
    private ExpiryVolatilityAdaptor expiryVolatilityAdaptor;
    private MarketDataAdaptor marketDataAdaptor;

    private PrimaryGreekAdaptor primaryGreekAdaptor;
    private SecondaryGreekAdaptor secondaryGreekAdaptor;
    private AdjustedGreekAdaptor adjustedGreekAdaptor;

    private final AeronManager aeronManager;

    public void start() {
        // transport
        // aeronManager.start();
        Aeron aeron = aeronManager.getAeron();
        AeronArchive aeronArchive = aeronManager.getAeronArchive();
        
        
        // transport output adaptors
        adjustedGreekAdaptor = new AdjustedGreekAdaptor(aeron, aeronArchive, nonTransportApplication.getAdjustedGreekRb());
        primaryGreekAdaptor = new PrimaryGreekAdaptor(aeron, aeronArchive, nonTransportApplication.getPrimaryGreekRb());
        secondaryGreekAdaptor = new SecondaryGreekAdaptor(aeron, aeronArchive, nonTransportApplication.getSecondaryGreekRb());
        adjustedGreekAdaptor.start();
        primaryGreekAdaptor.start();
        secondaryGreekAdaptor.start();
        
        // non transport application
        nonTransportApplication.start();

        // transport input adaptors
        strategyAdaptor = new StrategyAdaptor(aeronArchive, nonTransportApplication.getStrategyRb());
        instrumentAdaptor = new InstrumentAdaptor(aeronArchive, nonTransportApplication.getInstrumentRb());
        expiryVolatilityAdaptor = new ExpiryVolatilityAdaptor(aeronArchive, nonTransportApplication.getVolatilityRb());
        marketDataAdaptor = new MarketDataAdaptor(aeronArchive, nonTransportApplication.getMarketDataRb());
        strategyAdaptor.start();
        instrumentAdaptor.start();
        expiryVolatilityAdaptor.start();
        marketDataAdaptor.start();

        printThreadDump();
    }

    public void stop() {
        marketDataAdaptor.stop();
        expiryVolatilityAdaptor.stop();
        instrumentAdaptor.stop();
        strategyAdaptor.stop();
        
        nonTransportApplication.stop();
        
        secondaryGreekAdaptor.stop();
        primaryGreekAdaptor.stop();
        adjustedGreekAdaptor.stop();
        
        aeronManager.stop();
    }

    public static void printThreadDump() {
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMxBean.dumpAllThreads(true, true);

        logger.info("=== THREAD DUMP ===");
        for (ThreadInfo threadInfo : threadInfos) {
            logger.info("{}", threadInfo);
        }
    }
}
