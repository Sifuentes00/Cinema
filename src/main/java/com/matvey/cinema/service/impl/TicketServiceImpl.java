package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.repository.TicketRepository;
import com.matvey.cinema.service.TicketService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

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
        logger.info("Поиск билета с ID: {}", id);

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Билет с ID: {} найден в кэше.", id);
            return Optional.of((Ticket) cachedData.get());
        }

        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.ifPresent(value -> {
            cache.put(cacheKey, value);
            logger.info("Билет с ID: {} добавлен в кэш.", id);
        });

        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
        String cacheKey = CacheKeys.TICKETS_ALL;
        logger.info("Получение всех билетов.");

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Все билеты найдены в кэше.");
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findAll();
        cache.put(cacheKey, tickets);
        logger.info("Все билеты добавлены в кэш.");

        return tickets;
    }

    @Override
    public List<Ticket> findTicketsByUserUsername(String userUsername) {
        String cacheKey = CacheKeys.TICKETS_USER_PREFIX + userUsername;
        logger.info("Поиск билетов для пользователя: {}", userUsername);

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Билеты для пользователя '{}' найдены в кэше.", userUsername);
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findTicketsByUserUsername(userUsername);
        cache.put(cacheKey, tickets);
        logger.info("Билеты для пользователя '{}' добавлены в кэш.", userUsername);

        return tickets;
    }

    @Override
    public List<Ticket> findTicketsByShowtimeDateTime(String showtimeDateTime) {
        String cacheKey = CacheKeys.TICKETS_SHOWTIME_PREFIX + showtimeDateTime;
        logger.info("Поиск билетов для времени сеанса: {}", showtimeDateTime);

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Билеты для времени сеанса '{}' найдены в кэше.", showtimeDateTime);
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findTicketsByShowtimeDateTime(showtimeDateTime);
        cache.put(cacheKey, tickets);
        logger.info("Билеты для времени сеанса '{}' добавлены в кэш.", showtimeDateTime);

        return tickets;
    }

    @Override
    public List<Ticket> findTicketsBySeatId(Long seatId) {
        String cacheKey = CacheKeys.TICKETS_SEAT_PREFIX + seatId;
        logger.info("Поиск билетов для места с ID: {}", seatId);

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Билеты для места с ID: {} найдены в кэше.", seatId);
            return (List<Ticket>) cachedData.get();
        }

        List<Ticket> tickets = ticketRepository.findTicketsBySeatId(seatId);
        cache.put(cacheKey, tickets);
        logger.info("Билеты для места с ID: {} добавлены в кэш.", seatId);

        return tickets;
    }

    @Override
    public Ticket save(Ticket ticket) {
        Ticket savedTicket = ticketRepository.save(ticket);

        cache.evict(CacheKeys.TICKETS_ALL);
        cache.evict(CacheKeys.TICKET_PREFIX + savedTicket.getId());
        logger.info("Билет с ID: {} успешно сохранен и кэш очищен.", savedTicket.getId());

        Optional<Long> userIdOpt = ticketRepository.findUserIdById(savedTicket.getId());
        Optional<Long> showtimeIdOpt = ticketRepository.findShowtimeIdById(savedTicket.getId());
        Optional<Long> seatIdOpt = ticketRepository.findSeatIdById(savedTicket.getId());

        userIdOpt.ifPresent(userId -> {
            cache.evict(CacheKeys.TICKETS_USER_PREFIX + userId);
            logger.info("Кэш для билетов пользователя с ID '{}' очищен.", userId);
        });
        showtimeIdOpt.ifPresent(showtimeId -> {
            cache.evict(CacheKeys.TICKETS_SHOWTIME_PREFIX + showtimeId);
            logger.info("Кэш для билетов сеанса с ID '{}' очищен.", showtimeId);
        });
        seatIdOpt.ifPresent(seatId -> {
            cache.evict(CacheKeys.TICKETS_SEAT_PREFIX + seatId);
            logger.info("Кэш для билетов места с ID '{}' очищен.", seatId);
        });

        return savedTicket;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Удаление билета с ID: {}", id);
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        ticketOpt.ifPresent(ticket -> {
            cache.evict(CacheKeys.TICKETS_ALL);
            cache.evict(CacheKeys.TICKET_PREFIX + ticket.getId());

            Optional<Long> userIdOpt = ticketRepository.findUserIdById(ticket.getId());
            Optional<Long> showtimeIdOpt = ticketRepository.findShowtimeIdById(ticket.getId());
            Optional<Long> seatIdOpt = ticketRepository.findSeatIdById(ticket.getId());

            userIdOpt.ifPresent(userId -> {
                cache.evict(CacheKeys.TICKETS_USER_PREFIX + userId);
                logger.info("Кэш для билетов пользователя с ID '{}' очищен при удалении билета.",
                        userId);
            });
            showtimeIdOpt.ifPresent(showtimeId -> {
                cache.evict(CacheKeys.TICKETS_SHOWTIME_PREFIX + showtimeId);
                logger.info("Кэш для билетов сеанса с ID '{}' очищен при удалении билета.",
                        showtimeId);
            });
            seatIdOpt.ifPresent(seatId -> {
                cache.evict(CacheKeys.TICKETS_SEAT_PREFIX + seatId);
                logger.info("Кэш для билетов места с ID '{}' очищен при удалении билета.", seatId);
            });

            logger.info("Билет с ID: {} успешно удален и кэш очищен.", ticket.getId());
        });

        ticketRepository.deleteById(id);
    }
}
