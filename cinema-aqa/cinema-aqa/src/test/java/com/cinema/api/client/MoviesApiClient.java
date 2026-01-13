package com.cinema.api.client;

import com.cinema.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MoviesApiClient {

    static {
        RestAssured.baseURI = TestConfig.getBaseUrl();
    }

    public Response getMovies() {
        return RestAssured
                .given()
                .when()
                .get("/api/movies")
                .then()
                .extract()
                .response();
    }
}
