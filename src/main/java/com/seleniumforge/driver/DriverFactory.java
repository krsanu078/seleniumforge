package com.seleniumforge.driver;

import com.seleniumforge.config.ConfigReader;
import com.seleniumforge.enums.BrowserType;
import com.seleniumforge.factories.BrowserFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * DriverFactory manages a ThreadLocal WebDriver for parallel-safe execution.
 */
public final class DriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
        // Utility
    }

    /**
     * Initialize WebDriver for the calling thread based on configuration.
     */
    public static void initDriver() {
        if (driver.get() == null) {
            ConfigReader config = ConfigReader.getInstance();
            String browser = config.getBrowser().toUpperCase();
            boolean headless = config.isHeadless();
            BrowserType browserType;
            try {
                if (browser.equals("HEADLESS_CHROME")) {
                    browserType = BrowserType.HEADLESS_CHROME;
                } else {
                    browserType = BrowserType.valueOf(browser);
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Unsupported browser '{}', falling back to CHROME", browser);
                browserType = BrowserType.CHROME;
            }
            LOGGER.info("Creating WebDriver for browser: {} (headless={})", browserType, headless);
            WebDriver wd = BrowserFactory.createDriver(browserType, headless);
            wd.manage().window().maximize();
            wd.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getTimeout()));
            wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getTimeout()));
            driver.set(wd);
            LOGGER.info("WebDriver created and stored in ThreadLocal");
        }
    }

    /**
     * Get WebDriver for the current thread.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Quit and remove WebDriver for the current thread.
     */
    public static void quitDriver() {
        WebDriver wd = driver.get();
        if (wd != null) {
            LOGGER.info("Quitting WebDriver for current thread");
            wd.quit();
            driver.remove();
        }
    }
}
