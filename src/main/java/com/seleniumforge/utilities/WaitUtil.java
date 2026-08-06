package com.seleniumforge.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtil provides explicit wait helpers to avoid Thread.sleep and make tests stable.
 * Single responsibility: centralized explicit wait utilities.
 */
public final class WaitUtil {

    private WaitUtil() {
        // utility class
    }

    /**
     * Wait until the element is visible on the page.
     *
     * @param driver  WebDriver instance
     * @param element WebElement to wait for
     * @param seconds timeout in seconds
     */
    public static void waitForVisibility(WebDriver driver, WebElement element, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait until the element is clickable.
     *
     * @param driver  WebDriver instance
     * @param element WebElement to wait for
     * @param seconds timeout in seconds
     */
    public static void waitForClickable(WebDriver driver, WebElement element, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait until the provided condition is met.
     *
     * @param driver    WebDriver instance
     * @param condition ExpectedCondition to wait for
     * @param seconds   timeout in seconds
     * @param <T>       return type of condition
     * @return condition result
     */
    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition, long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(condition);
    }

    /**
     * Wait until the element becomes invisible.
     *
     * @param driver  WebDriver instance
     * @param element WebElement to wait to be invisible
     * @param seconds timeout in seconds
     */
    public static void waitForInvisibility(WebDriver driver, WebElement element, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.invisibilityOf(element));
    }
}
