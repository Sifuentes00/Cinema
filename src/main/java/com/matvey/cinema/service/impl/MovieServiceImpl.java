package com.matvey.cinema.service.impl;

import com.matvey.cinema.dao.MovieDao;
import com.matvey.cinema.model.Movie;
import com.matvey.cinema.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao;

    public MovieServiceImpl(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieDao.findById(id);
    }

    @Override
    public List<Movie> findAll() {
        return movieDao.findAll();
    }
}
