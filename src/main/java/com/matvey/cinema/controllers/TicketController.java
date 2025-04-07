package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.TicketRequest;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.repository.TicketRepository;
import com.matvey.cinema.service.SeatService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.TicketService;
import com.matvey.cinema.service.UserService;
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
@Tag(name = "Ticket Controller", description = "API для управления билетами")
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final SeatService seatService;
    private final UserService userService;
    private final ShowtimeService showtimeService;
    private final TicketRepository ticketRepository;
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    public TicketController(TicketService ticketService, SeatService seatService,
                            UserService userService, ShowtimeService showtimeService,
                            TicketRepository ticketRepository) {
        this.ticketService = ticketService;
        this.seatService = seatService;
        this.userService = userService;
        this.showtimeService = showtimeService;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить билет по ID",
            description = "Возвращает билет с указанным ID")
    public ResponseEntity<Ticket> getTicketById(
            @Parameter(description = "Идентификатор билета", example = "1") @PathVariable Long id) {
        logger.debug("Запрос на получение билета с ID: {}", id);
        Optional<Ticket> ticket = ticketService.findById(id);
        return ticket.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.error("Билет с ID {} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    @Operation(summary = "Получить все билеты",
            description = "Возвращает список всех билетов в базе данных")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        logger.debug("Запрос на получение всех билетов");
        List<Ticket> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/user")
    @Operation(summary = "Получить билеты по имени пользователя",
            description = "Возвращает список билетов для указанного пользователя")
    public ResponseEntity<List<Ticket>> getTicketsByUserUsername(
            @Parameter(description = "Имя пользователя", example = "john_doe")
            @RequestParam String userUsername) {
        logger.debug("Запрос на получение билетов для пользователя");
        List<Ticket> tickets = ticketService.findTicketsByUserUsername(userUsername);
        if (tickets.isEmpty()) {
            logger.warn("Билеты для пользователя {} не найдены", userUsername);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/showtime")
    @Operation(summary = "Получить билеты по времени сеанса",
            description = "Возвращает список билетов для указанного времени сеанса")
    public ResponseEntity<List<Ticket>> getTicketsByShowtimeDateTime(
            @Parameter(description = "Время сеанса", example = "17.04.2025 13:00")
            @RequestParam String showtimeDateTime) {
        logger.debug("Запрос на получение билетов для времени сеанса");
        List<Ticket> tickets = ticketService.findTicketsByShowtimeDateTime(showtimeDateTime);
        if (tickets.isEmpty()) {
            logger.warn("Билеты для времени сеанса {} не найдены", showtimeDateTime);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/seat/{seatId}")
    @Operation(summary = "Получить билеты по ID места",
            description = "Возвращает список билетов для указанного места")
    public ResponseEntity<List<Ticket>> getTicketsBySeatId(
            @Parameter(description = "Идентификатор места", example = "1")
            @PathVariable Long seatId) {
        logger.debug("Запрос на получение билетов для места с ID: {}", seatId);
        List<Ticket> tickets = ticketService.findTicketsBySeatId(seatId);
        if (tickets.isEmpty()) {
            logger.warn("Билеты для места с ID {} не найдены", seatId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    @Operation(summary = "Создать новый билет",
            description = "Создает новый билет на основе предоставленных данных")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        logger.debug("Запрос на создание нового билета: {}", ticketRequest);
        Ticket ticket = new Ticket();

        ticketRepository.updateTicketDetails(ticket, ticketRequest,
                showtimeService, seatService, userService);

        Ticket savedTicket = ticketService.save(ticket);
        logger.info("Билет успешно создан с ID: {}", savedTicket.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить билет",
            description = "Обновляет существующий билет с указанным ID")
    public ResponseEntity<Ticket> updateTicket(
            @Parameter(description = "Идентификатор билета для обновления",
                    example = "1") @PathVariable Long id,
            @Valid @RequestBody TicketRequest ticketRequest) {
        logger.debug("Запрос на обновление билета с ID: {}", id);
        Ticket existingTicket = ticketService.findById(id)
                .orElseThrow(() -> {
                    logger.error("Билет не найден с ID: {}", id);
                    return new RuntimeException("Билет не найден с ID: " + id);
                });

        ticketRepository.updateTicketDetails(existingTicket, ticketRequest,
                showtimeService, seatService, userService);

        Ticket updatedTicket = ticketService.save(existingTicket);
        logger.info("Билет с ID: {} успешно обновлен", id);

        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить билет",
            description = "Удаляет билет с указанным ID")
    public ResponseEntity<Void> deleteTicket(
            @Parameter(description = "Идентификатор билета для удаления",
                    example = "1") @PathVariable Long id) {
        logger.debug("Запрос на удаление билета с ID: {}", id);
        ticketService.deleteById(id);
        logger.info("Билет с ID: {} успешно удален", id);
        return ResponseEntity.noContent().build();
    }
}
