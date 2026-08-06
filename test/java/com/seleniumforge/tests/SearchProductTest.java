package com.seleniumforge.tests;

import com.seleniumforge.listeners.TestListener;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import com.seleniumforge.pages.ProductPage;
import com.seleniumforge.retry.RetryAnalyzer;
import com.seleniumforge.tests.providers.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Verify opening a product and reading product details.
 */
@Listeners(TestListener.class)
public class SearchProductTest extends com.seleniumforge.base.BaseTest {

    @Test(dataProvider = "products", dataProviderClass = TestDataProvider.class, groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void openProductAndVerifyDetails(String productName) {
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        HomePage home = new HomePage();
        home.openProductByName(productName);
        ProductPage productPage = new ProductPage();
        Assert.assertEquals(productPage.getProductName(), productName, "Product name should match");
        Assert.assertTrue(productPage.getProductDescription().length() > 0, "Product description should be present");
    }
}
