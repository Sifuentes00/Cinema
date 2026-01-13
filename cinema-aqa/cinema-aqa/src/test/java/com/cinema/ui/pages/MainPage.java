package com.cinema.ui.pages;

import com.cinema.config.TestConfig;
import com.cinema.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {

    private final WebDriver driver;

    private final By header = By.tagName("h1");
    private final By movieCards = By.cssSelector(".movie-card");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        WaitUtils.waitForVisible(
                driver,
                header,
                TestConfig.getTimeoutSeconds()
        );
    }

    public boolean isOpened() {
        return driver.getTitle().contains("Cinema");
    }

    public String getHeaderText() {
        return driver.findElement(header).getText();
    }

    public int getMoviesCount() {
        return driver.findElements(movieCards).size();
    }

    public void openFirstMovie() {
        driver.findElements(movieCards).get(0).click();
    }
}
