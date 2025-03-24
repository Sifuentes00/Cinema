package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    List<Ticket> findTicketsByUserId(Long userId);

    List<Ticket> findTicketsByShowtimeId(Long showtimeId);

    List<Ticket> findTicketsBySeatId(Long seatId);

    Ticket save(Ticket ticket);

    void deleteById(Long id);
}
