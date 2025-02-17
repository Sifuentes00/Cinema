package com.matvey.cinema.services;

import com.matvey.cinema.model.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    Movie save(Movie movie); // Сохранить или обновить фильм

    Optional<Movie> findById(Long id); // Найти фильм по ID

    List<Movie> findAll(); // Получить все фильмы

    void deleteById(Long id); // Удалить фильм по ID
}