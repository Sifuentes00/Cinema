package com.matvey.cinema.controllers;

import com.matvey.cinema.model.Movie;
import com.matvey.cinema.service.MovieService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Получить фильм по ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.findById(id);
        return movie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Получить все фильмы
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    // Создать новый фильм
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.save(movie);
        return ResponseEntity.ok(createdMovie);
    }

    // Обновить существующий фильм
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        // Устанавливаем ID для обновляемого фильма
        movie.setId(id);
        Movie updatedMovie = movieService.save(movie);
        return ResponseEntity.ok(updatedMovie); // Возвращаем обновленный фильм
    }

    // Удалить фильм по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content
    }

    // Поиск фильмов по параметрам
    @GetMapping("/query")
    public ResponseEntity<List<Movie>> getMovieByQueryParams(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String genre) {

        List<Movie> movies = movieService.findByQueryParams(id, title,
                director, releaseYear != null ? releaseYear : 0, genre);
        return ResponseEntity.ok(movies);
    }
}
