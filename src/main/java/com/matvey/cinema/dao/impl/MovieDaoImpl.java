package com.matvey.cinema.dao.impl;

import com.matvey.cinema.dao.MovieDao;
import com.matvey.cinema.model.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MovieDaoImpl implements MovieDao {
    private final List<Movie> movies = new ArrayList<>();

    @Override
    public Optional<Movie> findById(Long id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(movies);
    }
}
