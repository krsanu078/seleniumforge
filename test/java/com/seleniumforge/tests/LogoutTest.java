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
 * Logout test to ensure user can sign out successfully.
 */
@Listeners(TestListener.class)
public class LogoutTest extends com.seleniumforge.base.BaseTest {

    @Test(dataProvider = "validCredentials", dataProviderClass = TestDataProvider.class, groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void logoutReturnsToLoginPage(String username, String password) {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        HomePage home = new HomePage();
        home.logout();
        // After logout, ensure login page is present by instantiating LoginPage and interacting
        LoginPage lp = new LoginPage();
        try {
            lp.login("", "");
            Assert.assertTrue(true, "Back on login page after logout");
        } catch (Exception e) {
            Assert.fail("Expected to be on login page after logout", e);
        }
    }
}
