package com.seleniumforge.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumforge.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * ExtentManager initializes and provides access to ExtentReports for the test run.
 * It also records run metadata and optionally archives the report for CI consumption.
 */
public final class ExtentManager {

    private static final Logger LOGGER = LogManager.getLogger(ExtentManager.class);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static ExtentReports extent;
    private static Path reportPath;
    private static Instant startTime;
    private static Instant endTime;

    private ExtentManager() {
        // utility
    }

    /**
     * Initialize ExtentReports instance. Safe to call multiple times.
     */
    public static synchronized void initReports() {
        if (extent != null) return;
        try {
            startTime = Instant.now();
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
            extent.setSystemInfo("Framework", "seleniumforge");
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
     * Flush and close the report. Call at the end of the run. This method also writes a metadata file
     * and will archive the report if configured to do so.
     */
    public static synchronized void flushReports() {
        if (extent != null) {
            try {
                extent.flush();
                endTime = Instant.now();
                LOGGER.info("ExtentReports flushed to {}", reportPath != null ? reportPath.toAbsolutePath() : "unknown");
                writeMetadata();
                boolean archive = Boolean.parseBoolean(ConfigReader.getInstance().getProperty("report.archiveOnFinish", "true"));
                if (archive) {
                    try {
                        com.seleniumforge.utilities.ReportArchiver.archiveReport(reportPath);
                    } catch (Exception e) {
                        LOGGER.warn("Failed to archive report: {}", e.getMessage());
                    }
                }
            } finally {
                extent = null;
            }
        }
    }

    private static void writeMetadata() {
        try {
            if (reportPath == null) return;
            Map<String, Object> meta = new HashMap<>();
            meta.put("reportPath", reportPath.toAbsolutePath().toString());
            meta.put("startTime", startTime.toString());
            meta.put("endTime", endTime.toString());
            meta.put("durationSeconds", Duration.between(startTime, endTime).getSeconds());
            Path metaFile = reportPath.getParent().resolve("last-run-metadata.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(metaFile.toFile(), meta);
            LOGGER.info("Report metadata written to {}", metaFile.toAbsolutePath());
        } catch (Exception e) {
            LOGGER.warn("Failed to write report metadata: {}", e.getMessage());
        }
    }
}
