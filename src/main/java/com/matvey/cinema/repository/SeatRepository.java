package com.matvey.cinema.repository;

import com.matvey.cinema.model.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    // Здесь можно добавлять дополнительные методы, если нужно
}
