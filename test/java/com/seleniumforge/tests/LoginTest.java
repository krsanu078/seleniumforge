package com.seleniumforge.tests;

import com.seleniumforge.listeners.TestListener;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import com.seleniumforge.retry.RetryAnalyzer;
import com.seleniumforge.tests.providers.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Smoke tests for basic authentication flow.
 */
@Listeners(TestListener.class)
public class LoginTest extends com.seleniumforge.base.BaseTest {

    @Test(dataProvider = "validCredentials", dataProviderClass = TestDataProvider.class, groups = {"smoke"}, priority = 1, retryAnalyzer = RetryAnalyzer.class)
    public void loginWithValidCredentials(String username, String password) {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        HomePage home = new HomePage();
        Assert.assertTrue(home.isLoaded(), "Home page should be loaded after valid login");
    }
}
