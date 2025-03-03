package com.matvey.cinema.repository;

import com.matvey.cinema.model.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Здесь можно добавлять дополнительные методы, если нужно
}
