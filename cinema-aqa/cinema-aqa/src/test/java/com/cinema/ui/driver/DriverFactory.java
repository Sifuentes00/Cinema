package com.cinema.ui.driver;

import com.cinema.config.BrowserType;
import com.cinema.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {

    public static WebDriver createDriver() {

        BrowserType browser = TestConfig.getBrowser();

        switch (browser) {
            case CHROME:
                return new ChromeDriver();
            case FIREFOX:
                return new FirefoxDriver();
            case SAFARI:
                return new SafariDriver();
            default:
                throw new IllegalStateException("Unsupported browser");
        }
    }
}
