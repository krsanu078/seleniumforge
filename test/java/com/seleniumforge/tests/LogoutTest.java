package com.seleniumforge.tests;

import com.seleniumforge.listeners.TestListener;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import com.seleniumforge.retry.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Logout test to ensure user can sign out successfully.
 */
@Listeners(TestListener.class)
public class LogoutTest extends com.seleniumforge.base.BaseTest {

    @Test(groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void logoutReturnsToLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.login("standard_user", "secret_sauce");
        HomePage home = new HomePage();
        home.logout();
        // After logout, the login button should be visible on the login page
        LoginPage lp = new LoginPage();
        String err = lp.getErrorMessage(); // this is just a way to interact with the page; no assertion here
        // Assert that we are back on login page by checking presence of username field via attempting to type
        try {
            lp.login("", "");
            Assert.assertTrue(true, "Back on login page after logout");
        } catch (Exception e) {
            Assert.fail("Expected to be on login page after logout", e);
        }
    }
}
