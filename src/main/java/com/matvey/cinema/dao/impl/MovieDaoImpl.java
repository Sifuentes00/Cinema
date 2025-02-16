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

    @Override
    public Movie save(Movie movie) {
        // Если фильм с таким ID уже существует, обновляем его, иначе добавляем новый
        Optional<Movie> existingMovie = findById(movie.getId());
        if (existingMovie.isPresent()) {
            updateMovie(movie);
        } else {
            addMovie(movie);
        }
        return movie; // Возвращаем сохраненный или обновленный фильм
    }

    @Override
    public void deleteById(Long id) {
        movies.removeIf(movie -> movie.getId().equals(id));
    }

    private void addMovie(Movie movie) {
        movies.add(movie);
    }

    private void updateMovie(Movie movie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(movie.getId())) {
                movies.set(i, movie);
                return;
            }
        }
    }
}