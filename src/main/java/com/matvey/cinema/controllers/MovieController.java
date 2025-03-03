package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.MovieRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ReviewService;
import com.matvey.cinema.service.ShowtimeService;
import java.util.ArrayList;
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

    public MovieController(MovieService movieService, ReviewService reviewService,
                           ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.showtimeService = showtimeService;
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
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setGenre(movieRequest.getGenre());

        List<Review> reviews = new ArrayList<>();
        for (Long reviewId : movieRequest.getReviewIds()) {
            Optional<Review> reviewOptional = reviewService.findById(reviewId);
            reviewOptional.ifPresent(reviews::add); // Добавляем отзыв, если он найден
        }
        movie.setReviews(reviews);

        // Получаем существующие сеансы по ID
        List<Showtime> showtimes = new ArrayList<>();
        for (Long showtimeId : movieRequest.getShowtimeIds()) {
            Optional<Showtime> showtimeOptional = showtimeService.findById(showtimeId);
            if (showtimeOptional.isPresent()) {
                showtimes.add(showtimeOptional.get());
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }
        movie.setShowtimes(showtimes);

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

        // Обновляем данные фильма
        existingMovie.setTitle(movieRequest.getTitle());
        existingMovie.setDirector(movieRequest.getDirector());
        existingMovie.setReleaseYear(movieRequest.getReleaseYear());
        existingMovie.setGenre(movieRequest.getGenre());

        // Получаем существующие отзывы по ID
        List<Review> reviews = new ArrayList<>();
        for (Long reviewId : movieRequest.getReviewIds()) {
            Optional<Review> reviewOptional = reviewService.findById(reviewId);
            reviewOptional.ifPresent(reviews::add); // Добавляем отзыв, если он найден
        }
        existingMovie.setReviews(reviews);

        // Получаем существующие сеансы по ID
        List<Showtime> showtimes = new ArrayList<>();
        for (Long showtimeId : movieRequest.getShowtimeIds()) {
            Optional<Showtime> showtimeOptional = showtimeService.findById(showtimeId);
            if (showtimeOptional.isPresent()) {
                showtimes.add(showtimeOptional.get());
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }
        existingMovie.setShowtimes(showtimes);

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
