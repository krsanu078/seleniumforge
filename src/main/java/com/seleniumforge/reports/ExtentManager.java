package com.seleniumforge.reports;

import com.seleniumforge.config.ConfigReader;
import com.seleniumforge.driver.DriverFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentManager initializes and provides access to ExtentReports for the test run.
 */
public final class ExtentManager {

    private static final Logger LOGGER = LogManager.getLogger(ExtentManager.class);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static ExtentReports extent;
    private static Path reportPath;

    private ExtentManager() {
        // utility
    }

    /**
     * Initialize ExtentReports instance. Safe to call multiple times.
     */
    public static synchronized void initReports() {
        if (extent != null) return;
        try {
            String timestamp = LocalDateTime.now().format(FORMAT);
            Path reportsDir = Path.of("reports");
            if (!Files.exists(reportsDir)) Files.createDirectories(reportsDir);
            reportPath = reportsDir.resolve("ExtentReport_" + timestamp + ".html");
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath.toFile());
            extent = new ExtentReports();
            extent.attachReporter(spark);
            // system info
            ConfigReader cfg = ConfigReader.getInstance();
            extent.setSystemInfo("Environment", cfg.getProperty("environment"));
            extent.setSystemInfo("Browser", cfg.getProperty("browser"));
            extent.setSystemInfo("Base URL", cfg.getProperty("url"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            LOGGER.info("ExtentReports initialized at {}", reportPath.toAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Failed to initialize ExtentReports: {}", e.getMessage(), e);
        }
    }

    /**
     * Create a test node in the report.
     *
     * @param testName name of the test
     * @return ExtentTest instance
     */
    public static ExtentTest createTest(String testName) {
        if (extent == null) initReports();
        return extent.createTest(testName);
    }

    /**
     * Flush and close the report. Call at the end of the run.
     */
    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            LOGGER.info("ExtentReports flushed to {}", reportPath != null ? reportPath.toAbsolutePath() : "unknown");
            extent = null;
        }
    }
}
