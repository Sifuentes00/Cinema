package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.repository.TheaterRepository;
import com.matvey.cinema.service.TheaterService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;
    private final InMemoryCache cache;

    @Autowired
    public TheaterServiceImpl(TheaterRepository theaterRepository, InMemoryCache cache) {
        this.theaterRepository = theaterRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Theater> findById(Long id) {
        String cacheKey = CacheKeys.THEATER_PREFIX + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((Theater) cachedData.get());
        }

        Optional<Theater> theater = theaterRepository.findById(id);

        theater.ifPresent(value -> cache.put(cacheKey, value));

        return theater;
    }

    @Override
    public List<Theater> findAll() {
        String cacheKey = CacheKeys.THEATERS_ALL;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Theater>) cachedData.get();
        }

        List<Theater> theaters = theaterRepository.findAll();

        cache.put(cacheKey, theaters);

        return theaters;
    }

    @Override
    public Theater save(Theater theater) {
        Theater savedTheater = theaterRepository.save(theater);

        cache.evict(CacheKeys.THEATERS_ALL);
        cache.evict(CacheKeys.THEATER_PREFIX + savedTheater.getId());

        return savedTheater;
    }

    @Override
    public void deleteById(Long id) {
        cache.evict(CacheKeys.THEATERS_ALL);
        cache.evict(CacheKeys.THEATER_PREFIX + id);

        theaterRepository.deleteById(id);
    }
}
