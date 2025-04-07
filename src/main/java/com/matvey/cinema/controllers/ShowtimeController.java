package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.ShowtimeRequest;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.repository.ShowtimeRepository;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.TheaterService;
import com.matvey.cinema.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Showtime Controller", description = "API для управления сеансами")
@RequestMapping("/api/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final TicketService ticketService;
    private final ShowtimeRepository showtimeRepository;
    private static final Logger logger = LoggerFactory.getLogger(ShowtimeController.class);

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
    @Operation(summary = "Получить сеанс по ID", description = "Возвращает сеанс с указанным ID")
    public ResponseEntity<Showtime> getShowtimeById(
            @Parameter(description = "Идентификатор сеанса", example = "1") @PathVariable Long id) {
        logger.debug("Запрос на получение сеанса с ID: {}", id);
        Optional<Showtime> showtime = showtimeService.findById(id);
        return showtime.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.error("Сеанс с ID {} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    @Operation(summary = "Получить все сеансы",
            description = "Возвращает список всех сеансов в базе данных")
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        logger.debug("Запрос на получение всех сеансов");
        List<Showtime> showtimes = showtimeService.findAll();
        return ResponseEntity.ok(showtimes);
    }

    @GetMapping("/theater")
    @Operation(summary = "Получить сеансы по названию театра",
            description = "Возвращает список сеансов для указанного театра")
    public ResponseEntity<List<Showtime>> getShowtimesByTheaterName(
            @RequestParam String theaterName) {
        logger.debug("Запрос на получение сеансов для театра");
        List<Showtime> showtimes = showtimeService.findShowtimesByTheaterName(theaterName);
        if (showtimes.isEmpty()) {
            logger.warn("Сеансы для театра {} не найдены", theaterName);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(showtimes);
    }

    @GetMapping("/movie")
    @Operation(summary = "Получить сеансы по названию фильма",
            description = "Возвращает список сеансов для указанного фильма")
    public ResponseEntity<List<Showtime>> getShowtimesByMovieTitle(
            @RequestParam String movieTitle) {
        logger.debug("Запрос на получение сеансов для фильма");
        List<Showtime> showtimes = showtimeService.findShowtimesByMovieTitle(movieTitle);
        if (showtimes.isEmpty()) {
            logger.warn("Сеансы для фильма {} не найдены", movieTitle);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(showtimes);
    }

    @PostMapping
    @Operation(summary = "Создать новый сеанс",
            description = "Создает новый сеанс на основе предоставленных данных")
    public ResponseEntity<Showtime> createShowtime(@Valid @RequestBody ShowtimeRequest
                                                               showtimeRequest) {
        logger.debug("Запрос на создание нового сеанса: {}", showtimeRequest);
        Showtime showtime = new Showtime();

        showtimeRepository.updateShowtimeDetails(showtime, showtimeRequest,
                movieService, theaterService, ticketService);

        Showtime savedShowtime = showtimeService.save(showtime);
        logger.info("Сеанс успешно создан с ID: {}", savedShowtime.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedShowtime);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить сеанс",
            description = "Обновляет существующий сеанс с указанным ID")
    public ResponseEntity<Showtime> updateShowtimeWithMovieAndTheater(
            @Parameter(description = "Идентификатор сеанса для обновления",
                    example = "1") @PathVariable Long id,
            @Valid @RequestBody ShowtimeRequest showtimeRequest) {
        logger.debug("Запрос на обновление сеанса с ID: {}", id);
        Showtime existingShowtime = showtimeService.findById(id)
                .orElseThrow(() -> {
                    logger.error("Сеанс не найден с ID: {}", id);
                    return new RuntimeException("Сеанс не найден с ID: " + id);
                });

        showtimeRepository.updateShowtimeDetails(existingShowtime, showtimeRequest,
                movieService, theaterService, ticketService);

        Showtime updatedShowtime = showtimeService.save(existingShowtime);
        logger.info("Сеанс с ID: {} успешно обновлен", id);

        return ResponseEntity.ok(updatedShowtime);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сеанс", description = "Удаляет сеанс с указанным ID")
    public ResponseEntity<Void> deleteShowtime(
            @Parameter(description = "Идентификатор сеанса для удаления",
                    example = "1") @PathVariable Long id) {
        logger.debug("Запрос на удаление сеанса с ID: {}", id);
        showtimeService.deleteById(id);
        logger.info("Сеанс с ID: {} успешно удален", id);
        return ResponseEntity.noContent().build();
    }
}

