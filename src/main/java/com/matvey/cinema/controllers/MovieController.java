package com.matvey.cinema.controllers;

import com.matvey.cinema.model.Movie;
import com.matvey.cinema.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    //http://localhost:8080/api/movies/1
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

    //http://localhost:8080/api/movies/query?id=1&title=Inception&director=Christopher%20Nolan&releaseYear=2010&genre=Sci-Fi
    @GetMapping("/query")
    public ResponseEntity<Movie> getMovieByQueryParams(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String director,
            @RequestParam int releaseYear,
            @RequestParam String genre) {

        List<Movie> movies = movieService.findAll();
        for (Movie movie : movies) {
            if (movie.getId().equals(id)
                    && movie.getTitle().equalsIgnoreCase(title)
                    && movie.getDirector().equalsIgnoreCase(director)
                    && movie.getReleaseYear() == releaseYear
                    && movie.getGenre().equalsIgnoreCase(genre)) {
                return ResponseEntity.ok(movie);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
