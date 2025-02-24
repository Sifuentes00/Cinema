package com.matvey.cinema.service.impl;

import com.matvey.cinema.dao.MovieDao;
import com.matvey.cinema.model.Movie;
import com.matvey.cinema.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

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

    @Override
    public Optional<Movie> findByQueryParams(Long id, String title, String director, int releaseYear, String genre) {
        List<Movie> movies = movieDao.findAll();
        for (Movie movie : movies) {
            if (movie.getId().equals(id)
                    && movie.getTitle().equalsIgnoreCase(title)
                    && movie.getDirector().equalsIgnoreCase(director)
                    && movie.getReleaseYear() == releaseYear
                    && movie.getGenre().equalsIgnoreCase(genre)) {
                return Optional.of(movie);
            }
        }
        return Optional.empty(); // Если фильм не найден, возвращаем пустой Optional
    }
}
