package com.seleniumforge.pages;

import com.seleniumforge.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

/**
 * HomePage representing the products listing on SauceDemo.
 */
public class HomePage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(HomePage.class);

    @FindBy(css = ".inventory_item")
    private List<WebElement> products;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    /**
     * Initialize HomePage elements.
     */
    public HomePage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Check whether the home page has loaded by verifying the presence of products.
     *
     * @return true if products are present
     */
    public boolean isLoaded() {
        boolean loaded = products != null && !products.isEmpty();
        LOGGER.debug("HomePage loaded: {} (product count={})", loaded, products == null ? 0 : products.size());
        return loaded;
    }

    /**
     * Open a product by its visible name.
     *
     * @param name product name to open
     */
    public void openProductByName(String name) {
        LOGGER.info("Opening product by name: {}", name);
        Optional<WebElement> match = products.stream()
                .filter(p -> {
                    try {
                        WebElement title = p.findElement(By.cssSelector(".inventory_item_name"));
                        return title.getText().trim().equalsIgnoreCase(name.trim());
                    } catch (Exception e) {
                        return false;
                    }
                }).findFirst();
        match.ifPresent(p -> p.findElement(By.cssSelector(".inventory_item_name")).click());
    }

    /**
     * Add product to cart by visible name.
     *
     * @param name product name
     */
    public void addProductToCartByName(String name) {
        LOGGER.info("Adding product to cart: {}", name);
        products.stream().filter(p -> {
            try {
                WebElement title = p.findElement(By.cssSelector(".inventory_item_name"));
                return title.getText().trim().equalsIgnoreCase(name.trim());
            } catch (Exception e) {
                return false;
            }
        }).findFirst().ifPresent(p -> {
            WebElement addBtn = p.findElement(By.cssSelector("button.btn_inventory"));
            click(addBtn);
        });
    }

    /**
     * Navigate to the Cart page.
     */
    public void goToCart() {
        LOGGER.info("Navigating to cart");
        click(cartLink);
    }

    /**
     * Logout the current user using the burger menu.
     */
    public void logout() {
        LOGGER.info("Logging out user via menu");
        click(menuButton);
        // wait for menu to reveal and then click logout
        waitForVisibility(logoutLink);
        click(logoutLink);
    }
}
