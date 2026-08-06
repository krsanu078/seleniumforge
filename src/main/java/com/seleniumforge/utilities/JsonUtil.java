package com.seleniumforge.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Map;

/**
 * JsonUtil provides methods to read JSON resources into POJOs or maps using Jackson.
 */
public final class JsonUtil {

    private static final Logger LOGGER = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
        // utility
    }

    /**
     * Read a JSON file from classpath and return as Map.
     *
     * @param resourcePath path under resources (e.g., data/sample.json)
     * @return Map representation or null on error
     */
    public static Map<String, Object> readJsonAsMap(String resourcePath) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                LOGGER.warn("JSON resource '{}' not found", resourcePath);
                return null;
            }
            return OBJECT_MAPPER.readValue(is, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            LOGGER.error("Failed to read JSON '{}': {}", resourcePath, e.getMessage(), e);
            return null;
        }
    }
}
