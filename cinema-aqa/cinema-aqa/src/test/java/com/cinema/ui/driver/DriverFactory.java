package com.cinema.ui.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {

    public static WebDriver createDriver() {
        // SafariDriver не требует установки внешнего драйвера
        WebDriver driver = new SafariDriver();
        driver.manage().window().maximize();
        return driver;
    }
}
