package com.seleniumforge.pages;

import com.seleniumforge.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * LoginPage represents the SauceDemo login screen and actions.
 */
public class LoginPage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(LoginPage.class);

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "h3[data-test='error']")
    private WebElement errorMessage;

    public LoginPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Perform login with provided credentials.
     *
     * @param username username
     * @param password password
     */
    public void login(String username, String password) {
        LOGGER.info("Logging in as {}", username);
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    /**
     * Check whether login page is loaded by verifying username field is visible.
     *
     * @return true if loaded
     */
    public boolean isLoaded() {
        try {
            waitForVisibility(usernameField);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check whether an error message is visible after attempting login.
     *
     * @return true if error is visible
     */
    public boolean isErrorVisible() {
        try {
            waitForVisibility(errorMessage, 5);
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
