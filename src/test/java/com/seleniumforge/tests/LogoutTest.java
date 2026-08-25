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
 * Verify logout from Home page using burger menu.
 */
public class LogoutTest extends BaseTest {

    @Test(groups = {"regression"})
    public void logoutWorks() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("users.json")) {
            List<Map<String, String>> users = mapper.readValue(is, new TypeReference<>() {});
            Map<String, String> standard = users.stream().filter(u -> "standard".equals(u.get("role"))).findFirst().orElseThrow();

            LoginPage loginPage = new LoginPage();
            loginPage.login(standard.get("username"), standard.get("password"));

            HomePage home = new HomePage();
            home.logout();

            // After logout, the login page should be shown again (LoginPage constructs and uses presence checks)
            LoginPage after = new LoginPage();
            Assert.assertTrue(after.isLoaded(), "Login page should be loaded after logout");
        }
    }
}
