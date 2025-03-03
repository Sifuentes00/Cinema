package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    Ticket save(Ticket ticket);

    void deleteById(Long id);
}
