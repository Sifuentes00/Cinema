package com.matvey.cinema.service;

import com.matvey.cinema.model.Seat;
import java.util.List;
import java.util.Optional;

public interface SeatService {
    Optional<Seat> findById(Long id);

    List<Seat> findAll();

    Seat save(Seat seat);

    void deleteById(Long id);
}
