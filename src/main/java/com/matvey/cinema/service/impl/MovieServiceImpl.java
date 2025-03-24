package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.repository.MovieRepository;
import com.matvey.cinema.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final InMemoryCache cache;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, InMemoryCache cache) {
        this.movieRepository = movieRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        String cacheKey = "movie_" + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((Movie) cachedData.get());
        }

        Optional<Movie> movie = movieRepository.findById(id);

        movie.ifPresent(value -> cache.put(cacheKey, value));

        return movie;
    }

    @Override
    public List<Movie> findAll() {
        String cacheKey = "movies_all";

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Movie>) cachedData.get();
        }

        List<Movie> movies = movieRepository.findAll();

        cache.put(cacheKey, movies);

        return movies;
    }

    @Override
    public Movie save(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);

        cache.evict("movies_all");
        cache.evict("movie_" + savedMovie.getId());

        return savedMovie;
    }

    @Override
    public void deleteById(Long id) {
        movieRepository.deleteById(id);

        cache.evict("movies_all");
        cache.evict("movie_" + id);
    }
}