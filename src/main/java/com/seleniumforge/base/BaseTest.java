package com.seleniumforge.base;

import com.seleniumforge.config.ConfigReader;
import com.seleniumforge.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * BaseTest contains setup and teardown logic shared by all tests.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected ConfigReader config;
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    /**
     * Initialize framework and WebDriver before tests in the class run.
     */
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        LOGGER.info("Test setup starting");
        config = ConfigReader.getInstance();
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        String baseUrl = config.getBaseUrl();
        if (baseUrl != null && !baseUrl.isBlank()) {
            LOGGER.info("Navigating to base URL: {}", baseUrl);
            driver.get(baseUrl);
        }
        LOGGER.info("Test setup completed");
    }

    /**
     * Tear down WebDriver after tests in the class complete.
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        LOGGER.info("Test teardown starting");
        DriverFactory.quitDriver();
        LOGGER.info("Test teardown completed");
    }
}
