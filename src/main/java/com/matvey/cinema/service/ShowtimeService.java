package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Showtime;
import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    Optional<Showtime> findById(Long id);

    List<Showtime> findAll();

    List<Showtime> findShowtimesByTheaterId(Long theaterId);

    List<Showtime> findShowtimesByMovieId(Long movieId);

    Showtime save(Showtime showtime);

    void deleteById(Long id);
}
