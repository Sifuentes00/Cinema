package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    Optional<Movie> findById(Long id);

    List<Movie> findAll();

    List<Movie> findByQueryParams(Long id, String title,
                                  String director, int releaseYear, String genre);

    Movie save(Movie movie);

    void deleteById(Long id);
}
