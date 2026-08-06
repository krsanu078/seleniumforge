package com.seleniumforge.utilities;

import com.seleniumforge.driver.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot utility to capture and return screenshot paths or Base64 strings for reporting.
 */
public final class ScreenshotUtil {

    private static final Logger LOGGER = LogManager.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
        // Utility
    }

    /**
     * Capture a PNG screenshot and save to screenshots directory.
     *
     * @param driver WebDriver instance
     * @param name   friendly name for the screenshot file
     * @return absolute path to screenshot file
     */
    public static String takeScreenshot(WebDriver driver, String name) {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                LOGGER.warn("Driver does not support screenshots");
                return null;
            }
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(TS_FORMAT);
            String fileName = String.format("%s_%s.png", name.replaceAll("\\s+", "_"), timestamp);
            Path screenshotsDir = Path.of("screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }
            Path dst = screenshotsDir.resolve(fileName);
            FileUtils.copyFile(src, dst.toFile());
            LOGGER.info("Screenshot saved to {}", dst.toAbsolutePath());
            return dst.toAbsolutePath().toString();
        } catch (IOException e) {
            LOGGER.error("Failed to capture screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Capture screenshot and return Base64 representation suitable for embedding in reports.
     *
     * @param driver WebDriver instance
     * @return base64 string of PNG image
     */
    public static String getBase64Screenshot(WebDriver driver) {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                LOGGER.warn("Driver does not support screenshots");
                return null;
            }
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            LOGGER.error("Failed to capture base64 screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Convenience: take screenshot for current thread's driver.
     *
     * @param name friendly name
     * @return absolute path or null
     */
    public static String takeScreenshot(String name) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            LOGGER.warn("No WebDriver available for screenshot");
            return null;
        }
        return takeScreenshot(driver, name);
    }
}
