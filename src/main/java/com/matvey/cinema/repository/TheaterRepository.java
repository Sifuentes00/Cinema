package com.matvey.cinema.repository;

import com.matvey.cinema.model.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    // Здесь можно добавлять дополнительные методы, если нужно
}
