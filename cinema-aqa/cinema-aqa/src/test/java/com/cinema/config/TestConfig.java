package com.cinema.config;

import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = TestConfig.class
                .getClassLoader()
                .getResourceAsStream("test.properties")) {

            properties.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Cannot load test.properties", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static BrowserType getBrowser() {
        return BrowserType.valueOf(
                properties.getProperty("browser").toUpperCase()
        );
    }

    public static int getTimeoutSeconds() {
        return Integer.parseInt(
                properties.getProperty("timeout.seconds")
        );
    }
}
