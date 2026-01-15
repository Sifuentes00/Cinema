package cinema.api.client;

import cinema.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MoviesApiClient {

    static {
        RestAssured.baseURI = TestConfig.BASE_URL();
    }

    public Response getAllMovies() {
        return RestAssured
                .given()
                .when()
                .get("/api/movies")
                .then()
                .extract()
                .response();
    }

    public Response getMovieById(long id) {
        return RestAssured
                .given()
                .when()
                .get("/api/movies/" + id)
                .then()
                .extract()
                .response();
    }

    public Response getMoviesWithInvalidPath() {
        return RestAssured
                .given()
                .when()
                .get("/api/movies-invalid")
                .then()
                .extract()
                .response();
    }
}
