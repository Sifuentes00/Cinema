package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    List<Ticket> findTicketsByUserUsername(String userUsername);

    List<Ticket> findTicketsByShowtimeDateTime(String showtimeDateTime);

    List<Ticket> findTicketsBySeatId(Long seatId);

    Ticket save(Ticket ticket);

    void deleteById(Long id);
}
