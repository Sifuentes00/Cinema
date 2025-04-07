package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.MovieRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.repository.MovieRepository;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ReviewService;
import com.matvey.cinema.service.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movie Controller", description = "API для управления фильмами")
public class MovieController {
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final ShowtimeService showtimeService;
    private final MovieRepository movieRepository;
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    public MovieController(MovieService movieService, ReviewService reviewService,
                           ShowtimeService showtimeService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.showtimeService = showtimeService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить фильм по ID", description = "Возвращает фильм с указанным ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                    description = "Фильм успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
        @ApiResponse(responseCode = "404",
                    description = "Фильм не найден", content = @Content)
    })
    public ResponseEntity<Movie> getMovieById(
            @Parameter(description = "Идентификатор фильма", example = "1") @PathVariable Long id) {
        logger.debug("Запрос на получение фильма с ID: {}", id);
        Optional<Movie> movie = movieService.findById(id);
        return movie.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.error("Фильм с ID {} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    @Operation(summary = "Получить все фильмы",
            description = "Возвращает список всех фильмов в базе данных")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список фильмов успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)))
    })
    public ResponseEntity<List<Movie>> getAllMovies() {
        logger.debug("Запрос на получение всех фильмов");
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    @Operation(summary = "Создать новый фильм",
            description = "Создает новый фильм на основе предоставленных данных")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Фильм успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
        @ApiResponse(responseCode = "400",
                description = "Неверные входные данные", content = @Content)
    })
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        logger.debug("Запрос на создание нового фильма: {}", movieRequest);
        Movie movie = new Movie();
        movieRepository.updateMovieDetails(movie, movieRequest, reviewService, showtimeService);
        Movie savedMovie = movieService.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить фильм",
            description = "Обновляет существующий фильм с указанным ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Фильм успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class))),
        @ApiResponse(responseCode = "400",
                    description = "Неверные входные данные", content = @Content),
        @ApiResponse(responseCode = "404", description = "Фильм не найден", content = @Content)
    })
    public ResponseEntity<Movie> updateMovie(
            @Parameter(description = "Идентификатор фильма для обновления",
                    example = "1") @PathVariable Long id,
            @Valid @RequestBody MovieRequest movieRequest) {
        logger.debug("Запрос на обновление фильма с ID: {}", id);
        Movie existingMovie = movieService.findById(id)
                .orElseGet(() -> {
                    logger.error("Фильм с ID {} не найден", id);
                    return null;
                });

        if (existingMovie == null) {
            return ResponseEntity.notFound().build();
        }

        movieRepository.updateMovieDetails(existingMovie, movieRequest,
                reviewService, showtimeService);
        Movie updatedMovie = movieService.save(existingMovie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить фильм", description = "Удаляет фильм с указанным ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
                description = "Фильм успешно удален", content = @Content),
        @ApiResponse(responseCode = "404",
                description = "Фильм не найден", content = @Content)
    })
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "Идентификатор фильма для удаления",
                    example = "1") @PathVariable Long id) {
        logger.debug("Запрос на удаление фильма с ID: {}", id);
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

