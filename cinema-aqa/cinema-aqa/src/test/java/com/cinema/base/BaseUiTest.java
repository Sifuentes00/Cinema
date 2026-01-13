package com.cinema.base;

import com.cinema.config.TestConfig;
import com.cinema.ui.driver.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
        driver.get(TestConfig.getBaseUrl());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
