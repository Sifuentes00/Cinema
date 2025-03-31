package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.repository.MovieRepository;
import com.matvey.cinema.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;
    private final InMemoryCache cache;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, InMemoryCache cache) {
        this.movieRepository = movieRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        String cacheKey = CacheKeys.MOVIE_PREFIX + id;
        logger.info("Поиск фильма с ID: {}", id);

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Фильм с ID: {} найден в кэше.", id);
            return Optional.of((Movie) cachedData.get());
        }

        Optional<Movie> movie = movieRepository.findById(id);
        movie.ifPresent(value -> {
            cache.put(cacheKey, value); // Кэшируем фильм
            logger.info("Фильм с ID: {} добавлен в кэш.", id);
        });

        return movie;
    }

    @Override
    public List<Movie> findAll() {
        String cacheKey = CacheKeys.MOVIES_ALL;
        logger.info("Получение всех фильмов.");

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Все фильмы найдены в кэше.");
            return (List<Movie>) cachedData.get();
        }

        List<Movie> movies = movieRepository.findAll();
        cache.put(cacheKey, movies); // Кэшируем список фильмов
        logger.info("Все фильмы добавлены в кэш.");

        return movies;
    }

    @Override
    public Movie save(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);

        // Очищаем кэш для всех фильмов и конкретного фильма
        cache.evict(CacheKeys.MOVIES_ALL);
        cache.evict(CacheKeys.MOVIE_PREFIX + savedMovie.getId());
        logger.info("Фильм с ID: {} успешно сохранен и кэш очищен.", savedMovie.getId());

        return savedMovie;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Удаление фильма с ID: {}", id);
        movieRepository.deleteById(id);

        // Очищаем кэш для всех фильмов и конкретного фильма
        cache.evict(CacheKeys.MOVIES_ALL);
        cache.evict(CacheKeys.MOVIE_PREFIX + id);
        logger.info("Фильм с ID: {} успешно удален и кэш очищен.", id);
    }
}
