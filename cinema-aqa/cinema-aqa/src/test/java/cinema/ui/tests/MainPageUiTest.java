package cinema.ui.tests;

import cinema.base.BaseUiTest;
import cinema.ui.pages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class MainPageUiTest extends BaseUiTest {

    @Test
    @DisplayName("Главная страница Cinema открывается")
    void mainPageShouldOpen() {
        driver.get("http://localhost:5173/"); // порт фронта

        MainPage mainPage = new MainPage(driver);

        Assertions.assertTrue(
                mainPage.isOpened(),
                "Главная страница Cinema не открылась"
        );
    }

    @Test
    @DisplayName("На главной странице отображается список фильмов")
    void moviesListShouldNotBeEmpty() {
        driver.get("http://localhost:5173/");

        MainPage mainPage = new MainPage(driver);

        int moviesCount = mainPage.getMoviesCount();

        Assertions.assertAll(
                () -> Assertions.assertTrue(
                        moviesCount > 0,
                        "Список фильмов пуст"
                ),
                () -> Assertions.assertTrue(
                        moviesCount < 100,
                        "Слишком много фильмов — возможно, ошибка backend"
                )
        );
    }

    @Test
    @DisplayName("Пользователь может открыть билеты первого фильма")
    void userCanOpenTicketsOfFirstMovie() {
        driver.get("http://localhost:5173/");

        MainPage mainPage = new MainPage(driver);

        Assertions.assertTrue(
                mainPage.getMoviesCount() > 0,
                "Нет фильмов для открытия билетов"
        );

        mainPage.openMovieTicketsByIndex(0);

        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/movies/"),
                "URL не изменился после клика на кнопку БИЛЕТЫ"
        );
    }

    @Test
    @DisplayName("Кнопка БИЛЕТЫ доступна у каждого фильма")
    void eachMovieShouldHaveTicketsButton() {
        driver.get("http://localhost:5173/");

        MainPage mainPage = new MainPage(driver);

        int moviesCount = mainPage.getMoviesCount();

        Assertions.assertTrue(
                moviesCount > 0,
                "Фильмы отсутствуют"
        );

        for (int i = 0; i < moviesCount; i++) {
            try {
                mainPage.openMovieTicketsByIndex(i);
            } catch (Exception e) {
                Assertions.fail("Кнопка БИЛЕТЫ отсутствует у фильма с индексом " + i, e);
            }
            driver.navigate().back();
            new MainPage(driver); // ждем загрузку после возврата
        }
    }

    @Test
    @DisplayName("Главная страница корректно перезагружается")
    void pageShouldBeReloadedCorrectly() {
        driver.get("http://localhost:5173/");

        MainPage mainPage = new MainPage(driver);

        int initialMoviesCount = mainPage.getMoviesCount();

        driver.navigate().refresh();

        MainPage reloadedPage = new MainPage(driver);
        int moviesAfterReload = reloadedPage.getMoviesCount();

        Assertions.assertEquals(
                initialMoviesCount,
                moviesAfterReload,
                "Количество фильмов изменилось после перезагрузки страницы"
        );
    }
}
