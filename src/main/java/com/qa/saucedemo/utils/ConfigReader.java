package com.qa.saucedemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads test configuration from src/test/resources/config/config.properties.
 * Centralizing config avoids hardcoded URLs/credentials scattered across step
 * definitions and page objects (a common code-smell in Selenium frameworks).
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_PATH = "src/test/resources/config/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            PROPERTIES.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        // System property override lets CI pass -Dbrowser=firefox without editing files
        String override = System.getProperty(key);
        if (override != null && !override.isEmpty()) {
            return override;
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
