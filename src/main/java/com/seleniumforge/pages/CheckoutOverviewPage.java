package com.seleniumforge.pages;

import com.seleniumforge.base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * CheckoutOverviewPage represents the overview step before finalizing the order.
 */
public class CheckoutOverviewPage extends BasePage {

    @FindBy(id = "finish")
    private WebElement finishButton;

    public CheckoutOverviewPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Finish checkout to complete the order.
     */
    public void finishCheckout() {
        click(finishButton);
    }
}
