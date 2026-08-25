package com.seleniumforge.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumforge.base.BaseTest;
import com.seleniumforge.pages.CartPage;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Verify adding product to cart works.
 */
public class AddToCartTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    public void addProductToCart() throws Exception {
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
            Assert.assertTrue(cart.getCartProductNames().contains("Sauce Labs Backpack"), "Cart should contain the added product");
        }
    }
}
