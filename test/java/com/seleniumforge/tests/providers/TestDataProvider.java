package com.seleniumforge.tests.providers;

import com.seleniumforge.utilities.JsonUtil;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TestDataProvider supplies DataProviders backed by JSON resources in src/test/resources.
 */
public final class TestDataProvider {

    private TestDataProvider() {
        // utility
    }

    @DataProvider(name = "validCredentials")
    public static Object[][] validCredentials() {
        List<Map<String, Object>> users = JsonUtil.readJsonAsList("users.json");
        if (users == null) return new Object[][]{{"standard_user", "secret_sauce"}};
        for (Map<String, Object> u : users) {
            if ("standard".equalsIgnoreCase((String) u.get("role"))) {
                return new Object[][]{{u.get("username"), u.get("password")}};
            }
        }
        // fallback
        return new Object[][]{{"standard_user", "secret_sauce"}};
    }

    @DataProvider(name = "invalidCredentialsJson")
    public static Object[][] invalidCredentialsJson() {
        List<Map<String, Object>> users = JsonUtil.readJsonAsList("users.json");
        List<Object[]> rows = new ArrayList<>();
        if (users != null) {
            for (Map<String, Object> u : users) {
                String role = (String) u.get("role");
                if (!"standard".equalsIgnoreCase(role)) {
                    rows.add(new Object[]{u.get("username"), u.get("password")});
                }
            }
        }
        if (rows.isEmpty()) {
            rows.add(new Object[]{"locked_out_user", "secret_sauce"});
        }
        return rows.toArray(new Object[0][]);
    }

    @DataProvider(name = "products")
    public static Object[][] products() {
        List<String> products = JsonUtil.readJsonAsStringList("products.json");
        if (products == null || products.isEmpty()) return new Object[][]{{"Sauce Labs Backpack"}};
        Object[][] out = new Object[products.size()][1];
        for (int i = 0; i < products.size(); i++) {
            out[i][0] = products.get(i);
        }
        return out;
    }
}
