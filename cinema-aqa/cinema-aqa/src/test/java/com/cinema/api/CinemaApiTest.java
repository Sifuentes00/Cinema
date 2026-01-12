package com.cinema.api;

import com.cinema.config.TestConfig;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CinemaApiTest {

    @Test
    void shouldReturnMoviesList() {

        given()
                .baseUri(TestConfig.API_URL)
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }
}
