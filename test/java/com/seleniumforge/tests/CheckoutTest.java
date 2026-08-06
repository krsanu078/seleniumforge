package com.seleniumforge.tests;

import com.seleniumforge.listeners.TestListener;
import com.seleniumforge.pages.*;
import com.seleniumforge.retry.RetryAnalyzer;
import com.seleniumforge.tests.providers.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * End-to-end checkout flow test.
 */
@Listeners(TestListener.class)
public class CheckoutTest extends com.seleniumforge.base.BaseTest {

    @Test(dataProvider = "products", dataProviderClass = TestDataProvider.class, groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void completeCheckoutFlow(String productName) {
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        HomePage home = new HomePage();
        home.addProductToCartByName(productName);
        home.goToCart();
        CartPage cart = new CartPage();
        cart.proceedToCheckout();
        CheckoutPage checkoutPage = new CheckoutPage();
        checkoutPage.enterCustomerInformation("John", "Doe", "12345");
        checkoutPage.continueToOverview();
        CheckoutOverviewPage overview = new CheckoutOverviewPage();
        overview.finishCheckout();
        CheckoutCompletePage complete = new CheckoutCompletePage();
        String header = complete.getCompleteHeaderText();
        Assert.assertTrue(header != null && header.toUpperCase().contains("THANK YOU"), "Checkout should complete with thank you message");
    }
}
