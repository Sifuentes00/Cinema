package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.repository.TicketRepository;
import com.matvey.cinema.service.TicketService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final InMemoryCache cache;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, InMemoryCache cache) {
        this.ticketRepository = ticketRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        String cacheKey = CacheKeys.TICKET_PREFIX + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((Ticket) cachedData.get());
        }

        Optional<Ticket> ticket = ticketRepository.findById(id);

        ticket.ifPresent(value -> cache.put(cacheKey, value));

        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
        String cacheKey = CacheKeys.TICKETS_ALL;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findAll();

        cache.put(cacheKey, tickets);

        return tickets;
    }

    @Override
    public List<Ticket> findTicketsByUserId(Long userId) {
        String cacheKey = CacheKeys.TICKETS_USER_PREFIX + userId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findTicketsByUserId(userId);

        cache.put(cacheKey, tickets);

        return tickets;
    }

    @Override
    public List<Ticket> findTicketsByShowtimeId(Long showtimeId) {
        String cacheKey = CacheKeys.TICKETS_SHOWTIME_PREFIX + showtimeId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findTicketsByShowtimeId(showtimeId);

        cache.put(cacheKey, tickets);

        return tickets;
    }

    @Override
    public List<Ticket> findTicketsBySeatId(Long seatId) {
        String cacheKey = CacheKeys.TICKETS_SEAT_PREFIX + seatId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findTicketsBySeatId(seatId);

        cache.put(cacheKey, tickets);

        return tickets;
    }

    @Override
    public Ticket save(Ticket ticket) {
        Ticket savedTicket = ticketRepository.save(ticket);

        cache.evict(CacheKeys.TICKETS_ALL);
        cache.evict(CacheKeys.TICKET_PREFIX + savedTicket.getId());

        Optional<Long> userIdOpt = ticketRepository.findUserIdById(savedTicket.getId());
        Optional<Long> showtimeIdOpt = ticketRepository.findShowtimeIdById(savedTicket.getId());
        Optional<Long> seatIdOpt = ticketRepository.findSeatIdById(savedTicket.getId());

        userIdOpt.ifPresent(userId -> cache.evict(CacheKeys.TICKETS_USER_PREFIX + userId));
        showtimeIdOpt.ifPresent(showtimeId -> cache.evict(CacheKeys.TICKETS_SHOWTIME_PREFIX + showtimeId));
        seatIdOpt.ifPresent(seatId -> cache.evict(CacheKeys.TICKETS_SEAT_PREFIX + seatId));

        return savedTicket;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        ticketOpt.ifPresent(ticket -> {
            cache.evict(CacheKeys.TICKETS_ALL);
            cache.evict(CacheKeys.TICKET_PREFIX + ticket.getId());

            Optional<Long> userIdOpt = ticketRepository.findUserIdById(ticket.getId());
            Optional<Long> showtimeIdOpt = ticketRepository.findShowtimeIdById(ticket.getId());
            Optional<Long> seatIdOpt = ticketRepository.findSeatIdById(ticket.getId());

            userIdOpt.ifPresent(userId -> cache.evict(CacheKeys.TICKETS_USER_PREFIX + userId));
            showtimeIdOpt.ifPresent(showtimeId -> cache.evict(CacheKeys.TICKETS_SHOWTIME_PREFIX + showtimeId));
            seatIdOpt.ifPresent(seatId -> cache.evict(CacheKeys.TICKETS_SEAT_PREFIX + seatId));
        });

        ticketRepository.deleteById(id);
    }
}