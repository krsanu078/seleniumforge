package com.seleniumforge.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentTest;
import com.seleniumforge.driver.DriverFactory;
import com.seleniumforge.reports.ExtentManager;
import com.seleniumforge.utilities.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * TestListener hooks into TestNG lifecycle to integrate logging, screenshots and reporting.
 * Enhanced to capture per-test durations and to attach screenshots on failure/skip.
 */
public class TestListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();
    private final Map<String, Instant> startTimes = new HashMap<>();

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("Test run starting: {}", context.getName());
        ExtentManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("Test run finished: {}", context.getName());
        ExtentManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.info("Starting test: {}", testName);
        ExtentTest test = ExtentManager.createTest(getDisplayName(result));
        currentTest.set(test);
        startTimes.put(getTestId(result), Instant.now());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test succeeded: {}", result.getMethod().getMethodName());
        ExtentTest test = currentTest.get();
        if (test != null) test.pass("Test passed");
        recordDuration(result);
        // optional capture on success
        boolean captureOnSuccess = Boolean.parseBoolean(com.seleniumforge.config.ConfigReader.getInstance().getProperty("report.captureOnSuccess", "false"));
        if (captureOnSuccess) {
            attachScreenshot(result, "success");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        ExtentTest test = currentTest.get();
        attachScreenshot(result, "failure");
        if (test != null) {
            String base64 = ScreenshotUtil.getBase64Screenshot(DriverFactory.getDriver());
            if (base64 != null) {
                test.fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } else {
                test.fail(result.getThrowable());
            }
        }
        recordDuration(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test skipped: {}", result.getMethod().getMethodName());
        ExtentTest test = currentTest.get();
        attachScreenshot(result, "skipped");
        if (test != null) test.skip(result.getThrowable());
        recordDuration(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // not used
    }

    private void attachScreenshot(ITestResult result, String tag) {
        try {
            String name = getDisplayName(result) + "_" + tag;
            String path = ScreenshotUtil.takeScreenshot(DriverFactory.getDriver(), name);
            ExtentTest test = currentTest.get();
            if (path != null && test != null) {
                test.info("Screenshot: " + path, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to attach screenshot: {}", e.getMessage());
        }
    }

    private void recordDuration(ITestResult result) {
        try {
            String id = getTestId(result);
            Instant start = startTimes.get(id);
            if (start != null) {
                Duration duration = Duration.between(start, Instant.now());
                LOGGER.info("Test {} duration: {} ms", getDisplayName(result), duration.toMillis());
                startTimes.remove(id);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to record duration: {}", e.getMessage());
        }
    }

    private String getTestId(ITestResult result) {
        return result.getTestContext().getName() + "::" + result.getMethod().getMethodName() + "::" + result.getStartMillis();
    }

    private String getDisplayName(ITestResult result) {
        return result.getTestContext().getName() + " - " + result.getMethod().getMethodName();
    }
}
