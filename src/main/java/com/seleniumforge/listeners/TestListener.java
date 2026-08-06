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

/**
 * TestListener hooks into TestNG lifecycle to integrate logging, screenshots and reporting.
 */
public class TestListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

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
        ExtentTest test = ExtentManager.createTest(testName);
        currentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test succeeded: {}", result.getMethod().getMethodName());
        ExtentTest test = currentTest.get();
        if (test != null) test.pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        ExtentTest test = currentTest.get();
        String base64 = ScreenshotUtil.getBase64Screenshot(DriverFactory.getDriver());
        if (test != null) {
            if (base64 != null) {
                test.fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } else {
                test.fail(result.getThrowable());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test skipped: {}", result.getMethod().getMethodName());
        ExtentTest test = currentTest.get();
        if (test != null) test.skip(result.getThrowable());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // not used
    }
}
