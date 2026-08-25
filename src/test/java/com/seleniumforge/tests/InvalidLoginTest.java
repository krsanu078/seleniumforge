package com.seleniumforge.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumforge.base.BaseTest;
import com.seleniumforge.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Verify invalid login displays an error.
 */
public class InvalidLoginTest extends BaseTest {

    @Test(groups = {"regression"})
    public void invalidLoginShowsError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("users.json")) {
            List<Map<String, String>> users = mapper.readValue(is, new TypeReference<>() {});
            Map<String, String> locked = users.stream().filter(u -> "locked".equals(u.get("role"))).findFirst().orElseThrow();

            LoginPage loginPage = new LoginPage();
            loginPage.login(locked.get("username"), locked.get("password"));

            Assert.assertTrue(loginPage.isErrorVisible(), "Error message should be visible for locked out user");
        }
    }
}
