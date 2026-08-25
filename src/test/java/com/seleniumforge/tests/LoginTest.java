package com.seleniumforge.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumforge.base.BaseTest;
import com.seleniumforge.pages.HomePage;
import com.seleniumforge.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Smoke test for successful login using standard_user credentials.
 */
public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    public void successfulLogin() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("users.json")) {
            List<Map<String, String>> users = mapper.readValue(is, new TypeReference<>() {});
            Map<String, String> standard = users.stream().filter(u -> "standard".equals(u.get("role"))).findFirst().orElseThrow();

            LoginPage loginPage = new LoginPage();
            loginPage.login(standard.get("username"), standard.get("password"));

            HomePage home = new HomePage();
            Assert.assertTrue(home.isLoaded(), "Home page should be loaded after successful login");
        }
    }
}
