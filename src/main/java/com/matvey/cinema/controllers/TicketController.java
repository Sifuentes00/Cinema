package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.TicketRequest;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.repository.TicketRepository;
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
    private final TicketRepository ticketRepository;

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
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();

        ticketRepository.updateTicketDetails(ticket, ticketRequest,
                showtimeService, seatService, userService);

        Ticket savedTicket = ticketService.save(ticket);

        return ResponseEntity.ok(savedTicket);
    }

    @PutMapping("{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id,
                                               @RequestBody TicketRequest ticketRequest) {
        Ticket existingTicket = ticketService.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        ticketRepository.updateTicketDetails(existingTicket, ticketRequest,
                showtimeService, seatService, userService);

        Ticket updatedTicket = ticketService.save(existingTicket);

        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
