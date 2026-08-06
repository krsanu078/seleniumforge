package com.seleniumforge.tests;

import com.seleniumforge.listeners.TestListener;
import com.seleniumforge.pages.CartPage;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import com.seleniumforge.retry.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests for removing items from cart.
 */
@Listeners(TestListener.class)
public class RemoveFromCartTest extends com.seleniumforge.base.BaseTest {

    @Test(groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void removeProductFromCart() {
        String productName = "Sauce Labs Backpack";
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        HomePage home = new HomePage();
        home.addProductToCartByName(productName);
        home.goToCart();
        CartPage cart = new CartPage();
        List<String> names = cart.getCartProductNames();
        Assert.assertTrue(names.contains(productName), "Cart should contain the added product before removal");
        cart.removeProductByName(productName);
        // small retry loop to allow UI update; prefer explicit waits in real world
        names = cart.getCartProductNames();
        Assert.assertFalse(names.contains(productName), "Cart should not contain the product after removal");
    }
}
