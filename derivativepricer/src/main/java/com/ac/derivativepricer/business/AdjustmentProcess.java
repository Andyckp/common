package com.ac.derivativepricer.business;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.data.AdjustedResultEvent;
import com.ac.derivativepricer.data.MarketDataEvent;
import com.ac.derivativepricer.data.PrimaryGreekEvent;
import com.ac.derivativepricer.process.EventLoop;
import static com.ac.derivativepricer.process.EventLoop.poll;
import com.ac.derivativepricer.process.Process;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.EventPoller.PollState;
import static com.lmax.disruptor.EventPoller.PollState.PROCESSING;
import com.lmax.disruptor.RingBuffer;

public class AdjustmentProcess implements Process {

    private static final Logger logger = LoggerFactory.getLogger(AdjustmentProcess.class);
    protected final RingBuffer<AdjustedResultEvent> adjustedResultRb;
    protected final Random rand = new Random();
    protected final EventLoop eventLoop;

    public AdjustmentProcess(
            RingBuffer<PrimaryGreekEvent> primaryGreekRb,
            RingBuffer<MarketDataEvent> marketDataRb,
            RingBuffer<AdjustedResultEvent> adjustedResultRb) {

        this.adjustedResultRb = adjustedResultRb;
        EventPoller<PrimaryGreekEvent> primaryGreekPoller = primaryGreekRb.newPoller();
        EventPoller<MarketDataEvent> marketDataPoller = marketDataRb.newPoller();

        this.eventLoop = new EventLoop(() -> {
            // while (true) {
            PollState marketDataPollerState = poll(marketDataPoller, this::onMarketData, logger, "MarketData");
            PollState primaryGreekPollerState = poll(primaryGreekPoller, this::onPrimaryGreek, logger, "PrimaryGreek");
            
            if (PROCESSING != primaryGreekPollerState 
                && PROCESSING != marketDataPollerState) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            // }
        }, AdjustmentProcess.class.getSimpleName(), logger);
    }

    @Override
    public void start() {
        this.eventLoop.start();
    }

    @Override
    public void stop() {
        this.eventLoop.stop();
    }

    public void onPrimaryGreek(PrimaryGreekEvent primaryGreekEvent) {
        publishRandom();
    }

    public void onMarketData(MarketDataEvent marketDataEvent) {
        publishRandom();
    }

    private void publishRandom() {
        adjustedResultRb.publishEvent((AdjustedResultEvent event, long sequence) -> {
            event.setStrategyId("strat 1".toCharArray());
            event.setInstrumentId("inst 1".toCharArray());
            event.setUnderlyingId("ul 1".toCharArray());
            event.setTheo(rand.nextDouble());
            event.setUnderlyingPrice(rand.nextDouble());
        });
    }
}
