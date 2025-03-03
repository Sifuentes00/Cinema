package com.matvey.cinema.repository;

import com.matvey.cinema.model.entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // Здесь можно добавлять дополнительные методы, если нужно
}
