package com.matvey.cinema.services.impl;

import com.matvey.cinema.dao.MovieDao;
import com.matvey.cinema.model.Movie;
import com.matvey.cinema.services.MovieService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao;

    @Autowired
    public MovieServiceImpl(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public Movie save(Movie movie) {
        return movieDao.save(movie); // Сохранить или обновить фильм
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieDao.findById(id); // Найти фильм по ID
    }

    @Override
    public List<Movie> findAll() {
        return movieDao.findAll(); // Получить все фильмы
    }

    @Override
    public void deleteById(Long id) {
        movieDao.deleteById(id); // Удалить фильм по ID
    }
}
