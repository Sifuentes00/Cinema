package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.SeatRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.repository.SeatRepository;
import com.matvey.cinema.service.SeatService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;
    private final TheaterService theaterService;
    private final TicketService ticketService;
    private final SeatRepository seatRepository;

    public SeatController(SeatService seatService, TheaterService theaterService,
                          TicketService ticketService, SeatRepository seatRepository) {
        this.seatService = seatService;
        this.theaterService = theaterService;
        this.ticketService = ticketService;
        this.seatRepository = seatRepository;
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

    @GetMapping("/theater")
    public ResponseEntity<List<Seat>> getSeatsByTheaterId(@RequestParam String theaterName) {
        List<Seat> seats = seatService.findSeatsByTheaterName(theaterName);
        if (seats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(seats);
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody SeatRequest seatRequest) {
        Seat seat = new Seat();

        seatRepository.updateSeatDetails(seat, seatRequest, theaterService, ticketService);

        Seat createdSeat = seatService.save(seat);

        return ResponseEntity.ok(createdSeat);
    }

    @PutMapping("{id}")
    public ResponseEntity<Seat> updateSeatWithTheaterAndTickets(
            @PathVariable Long id,
            @RequestBody SeatRequest seatRequest) {

        Seat existingSeat = seatService.findById(id)
                .orElseThrow(() -> new RuntimeException("Место не найдено с ID: " + id));

        seatRepository.updateSeatDetails(existingSeat, seatRequest, theaterService, ticketService);

        Seat updatedSeat = seatService.save(existingSeat);

        return ResponseEntity.ok(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

