package cinema.api.tests;

import cinema.api.client.MoviesApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class CinemaApiTest {

    private final MoviesApiClient moviesApiClient = new MoviesApiClient();

    @Test
    void shouldReturnMoviesList() {
        Response response = moviesApiClient.getAllMovies();

        response.then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void movieShouldContainRequiredFields() {
        Response response = moviesApiClient.getAllMovies();

        response.then()
                .statusCode(200)
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].description", notNullValue());
    }

    @Test
    void shouldReturnMovieById() {
        Response response = moviesApiClient.getMovieById(1);

        response.then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void shouldReturn404ForInvalidMovieId() {
        Response response = moviesApiClient.getMovieById(99999);

        response.then()
                .statusCode(404);
    }

    @Test
    void shouldReturn404ForInvalidEndpoint() {
        Response response = moviesApiClient.getMoviesWithInvalidPath();

        response.then()
                .statusCode(404);
    }
}
