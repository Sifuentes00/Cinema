package cinema.config;

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

    public static String BASE_URL() {
        return properties.getProperty("base.url");
    }

    public static String API_URL() {
        return properties.getProperty("api.url");
    }

    public static int TIMEOUT() {
        return Integer.parseInt(properties.getProperty("timeout.seconds"));
    }

    public static BrowserType getBrowser() {
        String browser = properties.getProperty("browser", "SAFARI").toUpperCase();
        return BrowserType.valueOf(browser);
    }

}
