package com.matvey.cinema.service.impl;

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

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> findByQueryParams(Long id, String title, String director,
                                         int releaseYear, String genre) {
        List<Movie> movies = movieRepository.findAll();

        return movies.stream()
                .filter(movie -> (id == null || movie.getId().equals(id))
                        && (title == null || movie.getTitle().equalsIgnoreCase(title))
                        && (director == null || movie.getDirector().equalsIgnoreCase(director))
                        && (releaseYear == 0 || movie.getReleaseYear() == releaseYear)
                        && (genre == null || movie.getGenre().equalsIgnoreCase(genre)))
                .toList(); // Возвращаем отфильтрованный список
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }
}
