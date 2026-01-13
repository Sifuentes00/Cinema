package com.cinema.ui.tests;

import com.cinema.base.BaseUiTest;
import com.cinema.ui.pages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainPageUiTest extends BaseUiTest {

    @Test
    void mainPageShouldLoad() {
        MainPage mainPage = new MainPage(driver);

        Assertions.assertTrue(mainPage.isOpened());
        Assertions.assertEquals("Cinema", mainPage.getHeaderText());
        Assertions.assertTrue(mainPage.getMoviesCount() > 0);
    }
}
