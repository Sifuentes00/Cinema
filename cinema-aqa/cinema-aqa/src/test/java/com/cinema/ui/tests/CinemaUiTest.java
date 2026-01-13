package com.cinema.ui.tests;

import com.cinema.config.TestConfig;
import com.cinema.ui.driver.DriverFactory;
import com.cinema.ui.pages.MainPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CinemaUiTest {

    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createDriver();
        driver.get("http://localhost:8080");
        mainPage = new MainPage(driver);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void mainPageShouldOpen() {
        Assertions.assertTrue(mainPage.isOpened());
    }
}
