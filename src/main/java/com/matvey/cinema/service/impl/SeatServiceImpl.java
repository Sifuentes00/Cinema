package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.repository.SeatRepository;
import com.matvey.cinema.service.SeatService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final InMemoryCache cache;

    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository, InMemoryCache cache) {
        this.seatRepository = seatRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Seat> findById(Long id) {
        String cacheKey = CacheKeys.SEAT_PREFIX + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((Seat) cachedData.get());
        }

        Optional<Seat> seat = seatRepository.findById(id);

        seat.ifPresent(value -> cache.put(cacheKey, value));

        return seat;
    }

    @Override
    public List<Seat> findAll() {
        String cacheKey = CacheKeys.SEATS_ALL;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Seat>) cachedData.get();
        }

        List<Seat> seats = seatRepository.findAll();

        cache.put(cacheKey, seats);

        return seats;
    }

    @Override
    public List<Seat> findSeatsByTheaterId(Long theaterId) {
        String cacheKey = CacheKeys.SEATS_THEATER_PREFIX + theaterId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Seat>) cachedData.get();
        }

        List<Seat> seats = seatRepository.findSeatsByTheaterId(theaterId);

        cache.put(cacheKey, seats);

        return seats;
    }

    @Override
    public Seat save(Seat seat) {
        Seat savedSeat = seatRepository.save(seat);

        Optional<Long> theaterIdOpt = seatRepository.findTheaterIdById(savedSeat.getId());

        cache.evict(CacheKeys.SEATS_ALL);
        cache.evict(CacheKeys.SEAT_PREFIX + savedSeat.getId());

        theaterIdOpt.ifPresent(theaterId -> cache.evict(CacheKeys.SEATS_THEATER_PREFIX + theaterId));

        return savedSeat;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Seat> seatOpt = seatRepository.findById(id);
        seatOpt.ifPresent(seat -> {
            Optional<Long> theaterIdOpt = seatRepository.findTheaterIdById(seat.getId());

            cache.evict(CacheKeys.SEATS_ALL);
            cache.evict(CacheKeys.SEAT_PREFIX + seat.getId());

            theaterIdOpt.ifPresent(theaterId -> cache.evict(CacheKeys.SEATS_THEATER_PREFIX + theaterId));
        });

        seatRepository.deleteById(id);
    }
}
