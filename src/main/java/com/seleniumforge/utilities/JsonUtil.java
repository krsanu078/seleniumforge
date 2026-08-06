package com.seleniumforge.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;
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

    /**
     * Read a JSON array file from classpath and return as a List of Maps.
     * Useful for reading arrays of objects such as users or records.
     *
     * @param resourcePath path under resources
     * @return list of maps or null on error
     */
    public static List<Map<String, Object>> readJsonAsList(String resourcePath) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                LOGGER.warn("JSON resource '{}' not found", resourcePath);
                return null;
            }
            return OBJECT_MAPPER.readValue(is, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            LOGGER.error("Failed to read JSON list '{}': {}", resourcePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Read a JSON array of strings from classpath and return as a List of Strings.
     *
     * @param resourcePath path under resources
     * @return list of strings or null on error
     */
    public static List<String> readJsonAsStringList(String resourcePath) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                LOGGER.warn("JSON resource '{}' not found", resourcePath);
                return null;
            }
            return OBJECT_MAPPER.readValue(is, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            LOGGER.error("Failed to read JSON string list '{}': {}", resourcePath, e.getMessage(), e);
            return null;
        }
    }
}
