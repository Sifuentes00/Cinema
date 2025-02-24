package com.matvey.cinema.dao.impl;

import com.matvey.cinema.dao.MovieDao;
import com.matvey.cinema.model.Movie;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MovieDaoImpl implements MovieDao {
    private final List<Movie> movies = Arrays.asList(
            new Movie(1L, "Inception", "Christopher Nolan", 2010, "Sci-Fi"),
            new Movie(2L, "The Godfather", "Francis Ford Coppola", 1972, "Crime"),
            new Movie(3L, "Pulp Fiction", "Quentin Tarantino", 1994, "Crime"),
            new Movie(4L, "The Dark Knight", "Christopher Nolan", 2008, "Action")
    );

    @Override
    public Optional<Movie> findById(Long id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Movie> findAll() {
        return movies;
    }
}
