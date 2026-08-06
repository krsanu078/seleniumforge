package com.seleniumforge.pages;

import com.seleniumforge.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CartPage models the shopping cart page and its actions.
 */
public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Get names of products present in the cart.
     *
     * @return list of product names
     */
    public List<String> getCartProductNames() {
        return cartItems.stream().map(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText()).collect(Collectors.toList());
    }

    /**
     * Remove a product from the cart by its name.
     *
     * @param name product name to remove
     */
    public void removeProductByName(String name) {
        Optional<WebElement> match = cartItems.stream().filter(item -> {
            try {
                String title = item.findElement(By.cssSelector(".inventory_item_name")).getText();
                return title.trim().equalsIgnoreCase(name.trim());
            } catch (Exception e) {
                return false;
            }
        }).findFirst();
        match.ifPresent(item -> {
            WebElement removeBtn = item.findElement(By.cssSelector("button.cart_button"));
            click(removeBtn);
        });
    }

    /**
     * Proceed to the checkout information page.
     */
    public void proceedToCheckout() {
        click(checkoutButton);
    }
}
