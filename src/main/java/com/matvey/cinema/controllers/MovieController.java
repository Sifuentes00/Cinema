package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.MovieRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.repository.MovieRepository;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ReviewService;
import com.matvey.cinema.service.ShowtimeService;
import java.util.List;
import java.util.Optional;
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
public class MovieController {
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final ShowtimeService showtimeService;
    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService, ReviewService reviewService,
                           ShowtimeService showtimeService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.showtimeService = showtimeService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.findById(id);
        return movie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.save(movie);
        return ResponseEntity.ok(savedMovie);
    }

    @PostMapping("/with")
    public ResponseEntity<Movie> createMovie(@RequestBody MovieRequest movieRequest) {
        Movie movie = new Movie();
        movieRepository.updateMovieDetails(movie, movieRequest, reviewService, showtimeService);
        Movie savedMovie = movieService.save(movie);
        return ResponseEntity.ok(savedMovie);
    }

    // Обновить существующий фильм
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        // Устанавливаем ID для обновляемого фильма
        movie.setId(id);
        Movie updatedMovie = movieService.save(movie);
        return ResponseEntity.ok(updatedMovie); // Возвращаем обновленный фильм
    }

    @PutMapping("/with/{id}")
    public ResponseEntity<Movie> updateMovieWithReviewsAndShowtimes(
            @PathVariable Long id,
            @RequestBody MovieRequest movieRequest) {

        // Находим существующий фильм по ID
        Movie existingMovie = movieService.findById(id)
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: " + id));

        movieRepository.updateMovieDetails(existingMovie, movieRequest, reviewService,
                                            showtimeService);

        // Сохраняем обновленный фильм
        Movie updatedMovie = movieService.save(existingMovie);
        return ResponseEntity.ok(updatedMovie);
    }

    // Удалить фильм по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content
    }

}
