package com.seleniumforge.pages;

import com.seleniumforge.base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * CheckoutCompletePage represents the final confirmation page after checkout.
 */
public class CheckoutCompletePage extends BasePage {

    @FindBy(css = ".complete-header")
    private WebElement completeHeader;

    @FindBy(id = "back-to-products")
    private WebElement backHomeButton;

    public CheckoutCompletePage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Get the completion header text displayed on the page.
     *
     * @return header text
     */
    public String getCompleteHeaderText() {
        return getText(completeHeader);
    }

    /**
     * Navigate back to the home/products page.
     */
    public void backToHome() {
        click(backHomeButton);
    }
}
