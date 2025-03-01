package com.matvey.cinema.repository;

import com.matvey.cinema.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Здесь можно добавлять дополнительные методы, если нужно
}
