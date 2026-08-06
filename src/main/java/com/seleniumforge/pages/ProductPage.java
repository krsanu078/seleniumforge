package com.seleniumforge/pages;

import com.seleniumforge.base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * ProductPage represents an individual product details page.
 */
public class ProductPage extends BasePage {

    @FindBy(css = ".inventory_details_name")
    private WebElement productName;

    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;

    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;

    @FindBy(css = "button.btn_primary")
    private WebElement addToCartButton;

    @FindBy(css = "button.inventory_details_back_button")
    private WebElement backToProductsButton;

    public ProductPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Get product name.
     *
     * @return product name text
     */
    public String getProductName() {
        return getText(productName);
    }

    /**
     * Get product description.
     *
     * @return product description text
     */
    public String getProductDescription() {
        return getText(productDescription);
    }

    /**
     * Get product price text.
     *
     * @return product price
     */
    public String getProductPrice() {
        return getText(productPrice);
    }

    /**
     * Add product to cart.
     */
    public void addToCart() {
        click(addToCartButton);
    }

    /**
     * Navigate back to products list.
     */
    public void backToProducts() {
        click(backToProductsButton);
    }
}
