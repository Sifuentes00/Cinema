package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.ShowtimeRequest;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.repository.ShowtimeRepository;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.TheaterService;
import com.matvey.cinema.service.TicketService;
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
    private final ShowtimeRepository showtimeRepository;

    public ShowtimeController(ShowtimeService showtimeService, MovieService movieService,
                              TheaterService theaterService, TicketService ticketService,
                              ShowtimeRepository showtimeRepository) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.ticketService = ticketService;
        this.showtimeRepository = showtimeRepository;
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

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<Showtime>> getShowtimesByTheaterId(@PathVariable Long theaterId) {
        List<Showtime> showtimes = showtimeService.findShowtimesByTheaterId(theaterId);
        if (showtimes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращает 204, если сеансов нет
        }
        return ResponseEntity.ok(showtimes); // Возвращает 200 и список сеансов
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Showtime>> getShowtimesByMovieId(@PathVariable Long movieId) {
        List<Showtime> showtimes = showtimeService.findShowtimesByMovieId(movieId);
        if (showtimes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращает 204, если сеансов нет
        }
        return ResponseEntity.ok(showtimes); // Возвращает 200 и список сеансов
    }

    @PostMapping
    public ResponseEntity<Showtime> createShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        Showtime showtime = new Showtime();

        showtimeRepository.updateShowtimeDetails(showtime, showtimeRequest,
                movieService, theaterService, ticketService);

        Showtime savedShowtime = showtimeService.save(showtime);

        return ResponseEntity.ok(savedShowtime);
    }

    @PutMapping("{id}")
    public ResponseEntity<Showtime> updateShowtimeWithMovieAndTheater(
            @PathVariable Long id,
            @RequestBody ShowtimeRequest showtimeRequest) {

        Showtime existingShowtime = showtimeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Сеанс не найден с ID: " + id));

        showtimeRepository.updateShowtimeDetails(existingShowtime, showtimeRequest,
                movieService, theaterService, ticketService);

        Showtime updatedShowtime = showtimeService.save(existingShowtime);

        return ResponseEntity.ok(updatedShowtime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
