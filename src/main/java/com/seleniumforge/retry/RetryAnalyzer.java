package com.seleniumforge.retry;

import com.seleniumforge.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer provides a simple retry mechanism for flaky tests.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private final int maxRetries;

    public RetryAnalyzer() {
        String v = ConfigReader.getInstance().getProperty("retryCount");
        int configured = 1;
        try {
            if (v != null) configured = Integer.parseInt(v);
        } catch (NumberFormatException ignored) {
        }
        this.maxRetries = Math.max(0, configured);
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetries) {
            retryCount++;
            LOGGER.info("Retrying test '{}'. Attempt {}/{}", result.getName(), retryCount, maxRetries);
            return true;
        }
        return false;
    }
}
