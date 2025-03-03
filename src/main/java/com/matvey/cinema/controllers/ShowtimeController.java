package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.ShowtimeRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.TheaterService;
import com.matvey.cinema.service.TicketService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final TicketService ticketService;

    public ShowtimeController(ShowtimeService showtimeService, MovieService movieService,
                              TheaterService theaterService, TicketService ticketService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        Optional<Showtime> showtime = showtimeService.findById(id);
        return showtime.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        List<Showtime> showtimes = showtimeService.findAll();
        return ResponseEntity.ok(showtimes);
    }

    @PostMapping
    public ResponseEntity<Showtime> createShowtime(@RequestBody Showtime showtime) {
        Showtime createdShowtime = showtimeService.save(showtime);
        return ResponseEntity.ok(createdShowtime);
    }

    @PostMapping("/with")
    public ResponseEntity<Showtime> createShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        Showtime showtime = new Showtime();
        showtime.setDateTime(showtimeRequest.getDateTime());
        showtime.setType(showtimeRequest.getType());

        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : showtimeRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add); // Добавляем билет, если он найден
        }
        showtime.setTickets(tickets);

        Showtime savedShowtime = showtimeService.save(showtime);

        Movie movie = movieService.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: "
                        + showtimeRequest.getMovieId()));

        Theater theater = theaterService.findById(showtimeRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found with id: "
                        + showtimeRequest.getTheaterId()));

        movie.getShowtimes().add(savedShowtime);
        theater.getShowtimes().add(savedShowtime);

        movieService.save(movie);
        theaterService.save(theater);

        return ResponseEntity.ok(savedShowtime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Showtime> updateShowtime(@PathVariable Long id,
                                                   @RequestBody Showtime showtime) {
        showtime.setId(id);
        Showtime updatedShowtime = showtimeService.save(showtime);
        return ResponseEntity.ok(updatedShowtime);
    }

    @PutMapping("/with/{id}")
    public ResponseEntity<Showtime> updateShowtimeWithMovieAndTheater(
            @PathVariable Long id, // ID сеанса, который нужно обновить
            @RequestBody ShowtimeRequest showtimeRequest) {

        // Найти существующий сеанс по ID
        Showtime existingShowtime = showtimeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Сеанс не найден с ID: " + id));

        // Найти фильм по ID
        Movie movie = movieService.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + showtimeRequest.getMovieId()));

        existingShowtime.setDateTime(showtimeRequest.getDateTime());
        existingShowtime.setType(showtimeRequest.getType());

        // Убедиться, что сеанс связан с фильмом
        if (!movie.getShowtimes().contains(existingShowtime)) {
            movie.getShowtimes().add(existingShowtime);
        }

        Theater theater = theaterService.findById(showtimeRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + showtimeRequest.getTheaterId()));
        if (!theater.getShowtimes().contains(existingShowtime)) {
            theater.getShowtimes().add(existingShowtime);
        }

        // Получить существующие билеты по ID
        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : showtimeRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add); // Добавляем билет, если он найден
        }
        existingShowtime.setTickets(tickets);

        // Сохранить фильм (каскадно сохранит сеанс)
        movieService.save(movie);

        // Сохранить театр
        theaterService.save(theater);

        // Сохранить сеанс
        Showtime updatedShowtime = showtimeService.save(existingShowtime);

        // Возвращаем обновленный сеанс
        return ResponseEntity.ok(updatedShowtime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
