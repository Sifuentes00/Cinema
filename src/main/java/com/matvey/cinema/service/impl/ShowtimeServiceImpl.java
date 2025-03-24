package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.repository.ShowtimeRepository;
import com.matvey.cinema.service.ShowtimeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final InMemoryCache cache;

    @Autowired
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository, InMemoryCache cache) {
        this.showtimeRepository = showtimeRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Showtime> findById(Long id) {
        String cacheKey = CacheKeys.SHOWTIME_PREFIX + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((Showtime) cachedData.get());
        }

        Optional<Showtime> showtime = showtimeRepository.findById(id);

        showtime.ifPresent(value -> cache.put(cacheKey, value));

        return showtime;
    }

    @Override
    public List<Showtime> findAll() {
        String cacheKey = CacheKeys.SHOWTIMES_ALL;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Showtime>) cachedData.get();
        }

        List<Showtime> showtimes = showtimeRepository.findAll();

        cache.put(cacheKey, showtimes);

        return showtimes;
    }

    @Override
    public List<Showtime> findShowtimesByTheaterId(Long theaterId) {
        String cacheKey = CacheKeys.SHOWTIMES_THEATER_PREFIX + theaterId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Showtime>) cachedData.get();
        }

        List<Showtime> showtimes = showtimeRepository.findShowtimesByTheaterId(theaterId);

        cache.put(cacheKey, showtimes);

        return showtimes;
    }

    @Override
    public List<Showtime> findShowtimesByMovieId(Long movieId) {
        String cacheKey = CacheKeys.SHOWTIMES_MOVIE_PREFIX + movieId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Showtime>) cachedData.get();
        }

        List<Showtime> showtimes = showtimeRepository.findShowtimesByMovieId(movieId);

        cache.put(cacheKey, showtimes);

        return showtimes;
    }

    @Override
    public Showtime save(Showtime showtime) {
        Showtime savedShowtime = showtimeRepository.save(showtime);

        cache.evict(CacheKeys.SHOWTIMES_ALL);
        cache.evict(CacheKeys.SHOWTIME_PREFIX + savedShowtime.getId());

        Optional<Long> theaterIdOpt = showtimeRepository.findTheaterIdById(savedShowtime.getId());
        Optional<Long> movieIdOpt = showtimeRepository.findMovieIdById(savedShowtime.getId());

        theaterIdOpt.ifPresent(theaterId -> cache.evict(CacheKeys.SHOWTIMES_THEATER_PREFIX + theaterId));
        movieIdOpt.ifPresent(movieId -> cache.evict(CacheKeys.SHOWTIMES_MOVIE_PREFIX + movieId));

        return savedShowtime;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(id);
        showtimeOpt.ifPresent(showtime -> {
            cache.evict(CacheKeys.SHOWTIMES_ALL);
            cache.evict(CacheKeys.SHOWTIME_PREFIX + showtime.getId());

            Optional<Long> theaterIdOpt = showtimeRepository.findTheaterIdById(showtime.getId());
            Optional<Long> movieIdOpt = showtimeRepository.findMovieIdById(showtime.getId());

            theaterIdOpt.ifPresent(theaterId -> cache.evict(CacheKeys.SHOWTIMES_THEATER_PREFIX + theaterId));
            movieIdOpt.ifPresent(movieId -> cache.evict(CacheKeys.SHOWTIMES_MOVIE_PREFIX + movieId));
        });

        showtimeRepository.deleteById(id);
    }
}