package com.seleniumforge.tests;

import com.seleniumforge.listeners.TestListener;
import com.seleniumforge.pages.LoginPage;
import com.seleniumforge.retry.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Negative login scenarios.
 */
@Listeners(TestListener.class)
public class InvalidLoginTest extends com.seleniumforge.base.BaseTest {

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce"},
                {"standard_user", "wrong_password"},
                {"", ""}
        };
    }

    @Test(dataProvider = "invalidCredentials", groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    public void invalidLoginShowsError(String username, String password) {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        String err = loginPage.getErrorMessage();
        Assert.assertTrue(err != null && !err.isBlank(), "Error message should be shown for invalid login");
    }
}
