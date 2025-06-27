package com.ac.derivativepricer;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.common.TestApplication;

public class StartupTest {
    private static final Logger logger = LoggerFactory.getLogger(StartupTest.class);
    @Test
    public void runIndefinitely() {
        TestApplication app = new TestApplication();
        app.start();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
