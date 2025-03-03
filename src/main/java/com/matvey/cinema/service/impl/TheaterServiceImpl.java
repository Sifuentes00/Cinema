package com.matvey.cinema.service.impl;

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

    @Autowired
    public TheaterServiceImpl(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @Override
    public Optional<Theater> findById(Long id) {
        return theaterRepository.findById(id);
    }

    @Override
    public List<Theater> findAll() {
        return theaterRepository.findAll();
    }

    @Override
    public Theater save(Theater theater) {
        return theaterRepository.save(theater);
    }

    @Override
    public void deleteById(Long id) {
        theaterRepository.deleteById(id);
    }
}
