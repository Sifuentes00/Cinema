package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.TicketRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.service.SeatService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.TicketService;
import com.matvey.cinema.service.UserService;
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
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final SeatService seatService;
    private final UserService userService;
    private final ShowtimeService showtimeService;

    public TicketController(TicketService ticketService, SeatService seatService,
                            UserService userService, ShowtimeService showtimeService) {
        this.ticketService = ticketService;
        this.seatService = seatService;
        this.userService = userService;
        this.showtimeService = showtimeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        return ticket.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        // Сохраняем Ticket
        Ticket savedTicket = ticketService.save(ticket);

        // Возвращаем созданный Ticket с HTTP-статусом 200 OK
        return ResponseEntity.ok(savedTicket);
    }

    @PostMapping("/with")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest) {
        // Находим Showtime, Seat и User по их ID
        Showtime showtime = showtimeService.findById(ticketRequest.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: "
                        + ticketRequest.getShowtimeId()));
        Seat seat = seatService.findById(ticketRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with id: "
                        + ticketRequest.getSeatId()));



        Ticket ticket = new Ticket();
        ticket.setPrice(ticketRequest.getPrice());

        Ticket savedTicket = ticketService.save(ticket);

        User user = userService.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: "
                        + ticketRequest.getUserId()));
        showtime.getTickets().add(savedTicket);
        seat.getTickets().add(savedTicket);
        user.getTickets().add(savedTicket);

        // Сохраняем обновленные сущности
        showtimeService.save(showtime);
        seatService.save(seat);
        userService.save(user);

        return ResponseEntity.ok(savedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        ticket.setId(id);
        Ticket updatedTicket = ticketService.save(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/with/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id,
                                               @RequestBody TicketRequest ticketRequest) {
        // Находим Ticket по его ID
        Ticket existingTicket = ticketService.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        // Находим Showtime, Seat и User по их ID
        Showtime newShowtime = showtimeService.findById(ticketRequest.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: "
                        + ticketRequest.getShowtimeId()));
        Seat newSeat = seatService.findById(ticketRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with id: "
                        + ticketRequest.getSeatId()));

        existingTicket.setPrice(ticketRequest.getPrice());

        User newUser = userService.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: "
                        + ticketRequest.getUserId()));
        newShowtime.getTickets().add(existingTicket);
        newSeat.getTickets().add(existingTicket);
        newUser.getTickets().add(existingTicket);

        ticketService.save(existingTicket);
        showtimeService.save(newShowtime);
        seatService.save(newSeat);
        userService.save(newUser);

        return ResponseEntity.ok(existingTicket);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
