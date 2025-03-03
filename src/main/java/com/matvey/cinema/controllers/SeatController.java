package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.SeatRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.service.SeatService;
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
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;
    private final TheaterService theaterService;
    private final TicketService ticketService;

    public SeatController(SeatService seatService, TheaterService theaterService,
                          TicketService ticketService) {
        this.seatService = seatService;
        this.theaterService = theaterService;
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
        Optional<Seat> seat = seatService.findById(id);
        return seat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        List<Seat> seats = seatService.findAll();
        return ResponseEntity.ok(seats);
    }

    @PostMapping("/with")
    public ResponseEntity<Seat> createSeat(@RequestBody SeatRequest seatRequest) {
        Seat seat = new Seat();
        seat.setSeatRow(seatRequest.getSeatRow());
        seat.setNumber(seatRequest.getNumber());
        seat.setAvailable(seatRequest.isAvailable());

        // Найти театр по ID
        Theater theater = theaterService.findById(seatRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + seatRequest.getTheaterId()));
        // Установить связь с театром
        theater.getSeats().add(seat);

        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : seatRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add); // Добавляем билет, если он найден
        }
        seat.setTickets(tickets);

        // Сохранить изменения
        theaterService.save(theater);

        Seat createdSeat = seatService.save(seat);

        return ResponseEntity.ok(createdSeat);
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        Seat createdSeat = seatService.save(seat);
        return ResponseEntity.ok(createdSeat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Long id, @RequestBody Seat seat) {
        seat.setId(id);
        Seat updatedSeat = seatService.save(seat);
        return ResponseEntity.ok(updatedSeat);
    }

    @PutMapping("/with/{id}")
    public ResponseEntity<Seat> updateSeatWithTheaterAndTickets(
            @PathVariable Long id, // ID места, которое нужно обновить
            @RequestBody SeatRequest seatRequest) {

        // Найти существующее место по ID
        Seat existingSeat = seatService.findById(id)
                .orElseThrow(() -> new RuntimeException("Место не найдено с ID: " + id));

        // Обновить данные места
        existingSeat.setSeatRow(seatRequest.getSeatRow());
        existingSeat.setNumber(seatRequest.getNumber());
        existingSeat.setAvailable(seatRequest.isAvailable());

        // Найти театр по ID
        Theater theater = theaterService.findById(seatRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + seatRequest.getTheaterId()));

        if (!theater.getSeats().contains(existingSeat)) {
            theater.getSeats().add(existingSeat);
        }

        // Получить существующие билеты по ID
        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : seatRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add); // Добавляем билет, если он найден
        }
        existingSeat.setTickets(tickets);

        // Сохранить театр (каскадно сохранит место)
        theaterService.save(theater);

        // Сохранить место
        Seat updatedSeat = seatService.save(existingSeat);

        // Возвращаем обновленное место
        return ResponseEntity.ok(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

