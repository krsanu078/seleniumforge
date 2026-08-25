package com.seleniumforge.pages;

import com.seleniumforge.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * CheckoutPage represents the customer information entry page (first step in checkout).
 */
public class CheckoutPage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(CheckoutPage.class);

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    public CheckoutPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Enter customer information required for checkout.
     *
     * @param firstName first name
     * @param lastName  last name
     * @param postal    postal / zip code
     */
    public void enterCustomerInformation(String firstName, String lastName, String postal) {
        LOGGER.info("Entering customer information: {} {} {}", firstName, lastName, postal);
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(postalCodeField, postal);
    }

    /**
     * Continue to the checkout overview page.
     */
    public void continueToOverview() {
        LOGGER.info("Continuing to checkout overview");
        click(continueButton);
    }
}
