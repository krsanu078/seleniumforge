package com.seleniumforge.base;

import com.seleniumforge.config.ConfigReader;
import com.seleniumforge.exceptions.FrameworkException;
import com.seleniumforge.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

/**
 * BasePage provides common, reusable WebDriver interactions for Page Objects.
 * All Page Objects should extend this class to use its helper methods.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final ConfigReader config;
    private static final Logger LOGGER = LogManager.getLogger(BasePage.class);

    /**
     * Construct BasePage using the ThreadLocal WebDriver from DriverFactory.
     */
    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        if (this.driver == null) {
            throw new FrameworkException("WebDriver instance is null. Ensure DriverFactory.initDriver() was called.");
        }
        this.config = ConfigReader.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getTimeout()));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Click element after waiting for it to be clickable.
     *
     * @param element element to click
     */
    public void click(WebElement element) {
        try {
            waitForClickable(element);
            element.click();
            LOGGER.info("Clicked element: {}", describe(element));
        } catch (Exception e) {
            LOGGER.error("click() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to click element", e);
        }
    }

    /**
     * Type text into element after waiting for visibility.
     *
     * @param element element to type into
     * @param text    text to enter
     */
    public void type(WebElement element, String text) {
        try {
            waitForVisibility(element);
            element.clear();
            element.sendKeys(text);
            LOGGER.info("Typed text into element: {} (text length={})", describe(element), text == null ? 0 : text.length());
        } catch (Exception e) {
            LOGGER.error("type() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to type into element", e);
        }
    }

    /**
     * Clear the element's text.
     *
     * @param element element to clear
     */
    public void clear(WebElement element) {
        try {
            waitForVisibility(element);
            element.clear();
            LOGGER.info("Cleared element: {}", describe(element));
        } catch (Exception e) {
            LOGGER.error("clear() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to clear element", e);
        }
    }

    /**
     * Get visible text of an element after waiting for visibility.
     *
     * @param element element to read text from
     * @return text content
     */
    public String getText(WebElement element) {
        try {
            waitForVisibility(element);
            String text = element.getText();
            LOGGER.info("Read text from element: {} -> {}", describe(element), text);
            return text;
        } catch (Exception e) {
            LOGGER.error("getText() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to get text from element", e);
        }
    }

    /**
     * Scroll the element into view using JavaScript.
     *
     * @param element element to scroll to
     */
    public void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            LOGGER.info("Scrolled into view: {}", describe(element));
        } catch (Exception e) {
            LOGGER.error("scrollIntoView() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to scroll element into view", e);
        }
    }

    /**
     * Hover over element using Actions.
     *
     * @param element element to hover
     */
    public void hover(WebElement element) {
        try {
            waitForVisibility(element);
            actions.moveToElement(element).perform();
            LOGGER.info("Hovered over element: {}", describe(element));
        } catch (Exception e) {
            LOGGER.error("hover() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to hover over element", e);
        }
    }

    /**
     * Select dropdown option by visible text.
     *
     * @param element      select element
     * @param visibleText  visible text to select
     */
    public void selectDropdownByVisibleText(WebElement element, String visibleText) {
        try {
            waitForVisibility(element);
            Select select = new Select(element);
            select.selectByVisibleText(visibleText);
            LOGGER.info("Selected '{}' from dropdown: {}", visibleText, describe(element));
        } catch (Exception e) {
            LOGGER.error("selectDropdownByVisibleText() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to select dropdown by visible text", e);
        }
    }

    /**
     * Select dropdown option by value.
     *
     * @param element select element
     * @param value   option value
     */
    public void selectDropdownByValue(WebElement element, String value) {
        try {
            waitForVisibility(element);
            Select select = new Select(element);
            select.selectByValue(value);
            LOGGER.info("Selected value='{}' from dropdown: {}", value, describe(element));
        } catch (Exception e) {
            LOGGER.error("selectDropdownByValue() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to select dropdown by value", e);
        }
    }

    /**
     * Select dropdown option by index.
     *
     * @param element select element
     * @param index   option index
     */
    public void selectDropdownByIndex(WebElement element, int index) {
        try {
            waitForVisibility(element);
            Select select = new Select(element);
            select.selectByIndex(index);
            LOGGER.info("Selected index={} from dropdown: {}", index, describe(element));
        } catch (Exception e) {
            LOGGER.error("selectDropdownByIndex() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to select dropdown by index", e);
        }
    }

    /**
     * Click an element using JavaScript.
     *
     * @param element element to click
     */
    public void jsClick(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            LOGGER.info("Performed jsClick on element: {}", describe(element));
        } catch (Exception e) {
            LOGGER.error("jsClick() failed on element {}: {}", describe(element), e.getMessage());
            throw new FrameworkException("Failed to jsClick element", e);
        }
    }

    /**
     * Wait for element visibility using default timeout from ConfigReader.
     *
     * @param element element to wait for
     */
    public void waitForVisibility(WebElement element) {
        waitForVisibility(element, config.getTimeout());
    }

    /**
     * Wait for element visibility with explicit timeout.
     *
     * @param element element to wait for
     * @param seconds timeout in seconds
     */
    public void waitForVisibility(WebElement element, long seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOf(element));
            LOGGER.debug("Element visible: {}", describe(element));
        } catch (TimeoutException e) {
            LOGGER.error("waitForVisibility() timed out on element {}", describe(element));
            throw e;
        }
    }

    /**
     * Wait for element to be clickable using default timeout.
     *
     * @param element element to wait for
     */
    public void waitForClickable(WebElement element) {
        waitForClickable(element, config.getTimeout());
    }

    /**
     * Wait for element to be clickable with explicit timeout.
     *
     * @param element element to wait for
     * @param seconds timeout in seconds
     */
    public void waitForClickable(WebElement element, long seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.elementToBeClickable(element));
            LOGGER.debug("Element clickable: {}", describe(element));
        } catch (TimeoutException e) {
            LOGGER.error("waitForClickable() timed out on element {}", describe(element));
            throw e;
        }
    }

    /**
     * Wait for page load completion using document.readyState.
     *
     * @param seconds timeout in seconds
     */
    public void waitForPageLoad(long seconds) {
        try {
            ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete");
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(pageLoadCondition);
            LOGGER.debug("Page load complete");
        } catch (TimeoutException e) {
            LOGGER.error("waitForPageLoad() timed out after {} seconds", seconds);
            throw e;
        }
    }

    /**
     * Switch to frame by WebElement.
     *
     * @param frameElement frame WebElement
     */
    public void switchToFrame(WebElement frameElement) {
        try {
            driver.switchTo().frame(frameElement);
            LOGGER.info("Switched to frame element");
        } catch (NoSuchFrameException e) {
            LOGGER.error("switchToFrame() failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Switch to frame by name or id.
     *
     * @param nameOrId frame name or id
     */
    public void switchToFrame(String nameOrId) {
        try {
            driver.switchTo().frame(nameOrId);
            LOGGER.info("Switched to frame '{}'");
        } catch (NoSuchFrameException e) {
            LOGGER.error("switchToFrame() failed for '{}' : {}", nameOrId, e.getMessage());
            throw e;
        }
    }

    /**
     * Switch to default content from a frame.
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        LOGGER.info("Switched to default content");
    }

    /**
     * Switch to window by exact title.
     *
     * @param title window title to match
     * @return true if switched, false otherwise
     */
    public boolean switchToWindow(String title) {
        String original = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String h : handles) {
            driver.switchTo().window(h);
            if (driver.getTitle().equals(title)) {
                LOGGER.info("Switched to window with title: {}", title);
                return true;
            }
        }
        driver.switchTo().window(original);
        LOGGER.warn("Window with title '{}' not found", title);
        return false;
    }

    /**
     * Switch to window by handle.
     *
     * @param handle window handle
     */
    public void switchToWindowByHandle(String handle) {
        driver.switchTo().window(handle);
        LOGGER.info("Switched to window with handle: {}", handle);
    }

    /**
     * Accept browser alert if present.
     */
    public void acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
            LOGGER.info("Alert accepted");
        } catch (TimeoutException e) {
            LOGGER.warn("No alert present to accept");
        }
    }

    /**
     * Dismiss browser alert if present.
     */
    public void dismissAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
            LOGGER.info("Alert dismissed");
        } catch (TimeoutException e) {
            LOGGER.warn("No alert present to dismiss");
        }
    }

    /**
     * Get text of alert if present.
     *
     * @return alert text or null if no alert present
     */
    public String getAlertText() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            LOGGER.info("Alert text: {}", text);
            return text;
        } catch (TimeoutException e) {
            LOGGER.warn("No alert present to get text");
            return null;
        }
    }

    /**
     * Helper to describe an element for logging purposes.
     * Attempts to fetch tag name, id, classes, and text if available.
     *
     * @param element web element
     * @return description string
     */
    protected String describe(WebElement element) {
        try {
            String tag = element.getTagName();
            String id = element.getAttribute("id");
            String cls = element.getAttribute("class");
            String text = element.getText();
            return String.format("<%s id='%s' class='%s' text='%s'>", tag, id, cls, text == null ? "" : text.replaceAll("\n", " "));
        } catch (StaleElementReferenceException e) {
            return "[stale element]";
        } catch (Exception e) {
            return "[unknown element]";
        }
    }
}
