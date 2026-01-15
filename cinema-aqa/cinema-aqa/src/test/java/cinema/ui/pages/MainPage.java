package cinema.ui.pages;

import cinema.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class MainPage {

    private final WebDriver driver;

    // ====== Локаторы ======
    // Надежный XPath для кнопки "Добавить фильм"
    private final By addMovieButton = By.xpath("//button[contains(., 'Добавить фильм')]");
    private final By tableRows = By.cssSelector("tbody tr");
    private final By ticketsButtonInRow = By.xpath(".//button[contains(., 'БИЛЕТЫ')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        // Ждём загрузку DOM и появления кнопки "Добавить фильм"
        WaitUtils.waitForVisible(driver, addMovieButton);
    }

    // ====== Проверки ======
    public boolean isOpened() {
        return driver.findElements(addMovieButton).size() == 1;
    }

    public int getMoviesCount() {
        return driver.findElements(tableRows).size();
    }

    // ====== Действия ======
    public void openMovieTicketsByIndex(int index) {
        List<WebElement> rows = driver.findElements(tableRows);
        if (index >= 0 && index < rows.size()) {
            rows.get(index)
                    .findElement(ticketsButtonInRow)
                    .click();
        }
    }
}
