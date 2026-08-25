package com.seleniumforge.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumforge.base.BaseTest;
import com.seleniumforge.pages.CartPage;
import com.seleniumforge.pages.CheckoutCompletePage;
import com.seleniumforge.pages.CheckoutOverviewPage;
import com.seleniumforge.pages.CheckoutPage;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * End-to-end checkout test.
 */
public class CheckoutTest extends BaseTest {

    @Test(groups = {"regression"})
    public void completeCheckoutFlow() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("users.json")) {
            List<Map<String, String>> users = mapper.readValue(is, new TypeReference<>() {});
            Map<String, String> standard = users.stream().filter(u -> "standard".equals(u.get("role"))).findFirst().orElseThrow();

            LoginPage loginPage = new LoginPage();
            loginPage.login(standard.get("username"), standard.get("password"));

            HomePage home = new HomePage();
            home.addProductToCartByName("Sauce Labs Backpack");
            home.goToCart();

            CartPage cart = new CartPage();
            cart.proceedToCheckout();

            CheckoutPage checkout = new CheckoutPage();
            checkout.enterCustomerInformation("John", "Doe", "12345");
            checkout.continueToOverview();

            CheckoutOverviewPage overview = new CheckoutOverviewPage();
            overview.finishCheckout();

            CheckoutCompletePage complete = new CheckoutCompletePage();
            Assert.assertTrue(complete.getCompleteHeaderText().toLowerCase().contains("thank you"), "Completion page should show thank you message");
        }
    }
}
