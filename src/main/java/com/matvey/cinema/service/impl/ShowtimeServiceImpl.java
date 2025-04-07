package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.exception.CustomNotFoundException;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.repository.ShowtimeRepository;
import com.matvey.cinema.service.ShowtimeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {
    private static final Logger logger = LoggerFactory.getLogger(ShowtimeServiceImpl.class);

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
        logger.info("Поиск сеанса с ID: {}", id);

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Сеанс с ID: {} найден в кэше.", id);
            return Optional.of((Showtime) cachedData.get());
        }

        Optional<Showtime> showtime = showtimeRepository.findById(id);
        if (showtime.isEmpty()) {
            logger.error("Сеанс с ID: {} не найден.", id);
            throw new CustomNotFoundException("Сеанс не найден с ID: " + id);
        }

        showtime.ifPresent(value -> {
            cache.put(cacheKey, value);
            logger.info("Сеанс с ID: {} добавлен в кэш.", id);
        });

        return showtime;
    }

    @Override
    public List<Showtime> findAll() {
        String cacheKey = CacheKeys.SHOWTIMES_ALL;
        logger.info("Получение всех сеансов.");

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Все сеансы найдены в кэше.");
            return (List<Showtime>) cachedData.get();
        }

        List<Showtime> showtimes = showtimeRepository.findAll();
        cache.put(cacheKey, showtimes);
        logger.info("Все сеансы добавлены в кэш.");

        return showtimes;
    }

    @Override
    public List<Showtime> findShowtimesByTheaterName(String theaterName) {
        String cacheKey = CacheKeys.SHOWTIMES_THEATER_PREFIX + theaterName;
        logger.info("Поиск сеансов для театра");

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Сеансы для театра найдены в кэше.");
            return (List<Showtime>) cachedData.get();
        }

        List<Showtime> showtimes = showtimeRepository.findShowtimesByTheaterName(theaterName);
        cache.put(cacheKey, showtimes);
        logger.info("Сеансы для театра добавлены в кэш.");

        return showtimes;
    }

    @Override
    public List<Showtime> findShowtimesByMovieTitle(String movieTitle) {
        String cacheKey = CacheKeys.SHOWTIMES_MOVIE_PREFIX + movieTitle;
        logger.info("Поиск сеансов для фильма:");

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Сеансы для фильма найдены в кэше.");
            return (List<Showtime>) cachedData.get();
        }

        List<Showtime> showtimes = showtimeRepository.findShowtimesByMovieTitle(movieTitle);
        cache.put(cacheKey, showtimes);
        logger.info("Сеансы для фильма добавлены в кэш.");

        return showtimes;
    }

    @Override
    public Showtime save(Showtime showtime) {
        Showtime savedShowtime = showtimeRepository.save(showtime);

        cache.evict(CacheKeys.SHOWTIMES_ALL);
        cache.evict(CacheKeys.SHOWTIME_PREFIX + savedShowtime.getId());

        Optional<Long> theaterIdOpt = showtimeRepository.findTheaterIdById(savedShowtime.getId());
        Optional<Long> movieIdOpt = showtimeRepository.findMovieIdById(savedShowtime.getId());

        theaterIdOpt.ifPresent(theaterId -> {
            cache.evict(CacheKeys.SHOWTIMES_THEATER_PREFIX + theaterId);
            logger.info("Кэш для сеансов театра с ID '{}' очищен.", theaterId);
        });
        movieIdOpt.ifPresent(movieId -> {
            cache.evict(CacheKeys.SHOWTIMES_MOVIE_PREFIX + movieId);
            logger.info("Кэш для сеансов фильма с ID '{}' очищен.", movieId);
        });

        logger.info("Сеанс с ID: {} успешно сохранен и кэш очищен.", savedShowtime.getId());
        return savedShowtime;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Удаление сеанса с ID: {}", id);
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(id);
        if (showtimeOpt.isEmpty()) {
            logger.error("Сеанс с ID: {} не найден для удаления.", id);
            throw new CustomNotFoundException("Сеанс не найден с ID: " + id);
        }

        Showtime showtime = showtimeOpt.get();
        cache.evict(CacheKeys.SHOWTIMES_ALL);
        cache.evict(CacheKeys.SHOWTIME_PREFIX + showtime.getId());

        Optional<Long> theaterIdOpt = showtimeRepository.findTheaterIdById(showtime.getId());
        Optional<Long> movieIdOpt = showtimeRepository.findMovieIdById(showtime.getId());

        theaterIdOpt.ifPresent(theaterId -> {
            cache.evict(CacheKeys.SHOWTIMES_THEATER_PREFIX + theaterId);
            logger.info("Кэш для сеансов театра с ID '{}' очищен при удалении сеанса.", theaterId);
        });
        movieIdOpt.ifPresent(movieId -> {
            cache.evict(CacheKeys.SHOWTIMES_MOVIE_PREFIX + movieId);
            logger.info("Кэш для сеансов фильма с ID '{}' очищен при удалении сеанса.", movieId);
        });

        showtimeRepository.deleteById(id);
        logger.info("Сеанс с ID: {} успешно удален и кэш очищен.", showtime.getId());
    }
}

