package com.matvey.cinema.service;

import com.matvey.cinema.model.Showtime;
import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    Optional<Showtime> findById(Long id);

    List<Showtime> findAll();

    Showtime save(Showtime showtime);

    void deleteById(Long id);
}
