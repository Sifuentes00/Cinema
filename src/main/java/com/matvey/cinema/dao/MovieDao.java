package com.matvey.cinema.dao;

import com.matvey.cinema.model.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieDao {
    Optional<Movie> findById(Long id); // Найти фильм по ID

    List<Movie> findAll(); // Получить все фильмы
}
