package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.TheaterRequest;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.repository.TheaterRepository;
import com.matvey.cinema.service.SeatService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.TheaterService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {
    private final SeatService seatService;
    private final TheaterService theaterService;
    private final ShowtimeService showtimeService;
    private final TheaterRepository theaterRepository;

    public TheaterController(TheaterService theaterService, SeatService seatService,
                             ShowtimeService showtimeService, TheaterRepository theaterRepository) {
        this.theaterService = theaterService;
        this.seatService = seatService;
        this.showtimeService = showtimeService;
        this.theaterRepository = theaterRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable Long id) {
        Optional<Theater> theater = theaterService.findById(id);
        return theater.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        List<Theater> theaters = theaterService.findAll();
        return ResponseEntity.ok(theaters);
    }

    @PostMapping("/with")
    public ResponseEntity<Theater> createTheater(@RequestBody TheaterRequest theaterRequest) {
        Theater theater = new Theater();

        theaterRepository.updateTheaterDetails(theater, theaterRequest,
                seatService, showtimeService);

        Theater createdTheater = theaterService.save(theater);
        return ResponseEntity.ok(createdTheater);
    }

    @PostMapping
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        Theater createdTheater = theaterService.save(theater);
        return ResponseEntity.ok(createdTheater);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id,
                                                 @RequestBody Theater theater) {
        theater.setId(id);
        Theater updatedTheater = theaterService.save(theater);
        return ResponseEntity.ok(updatedTheater);
    }

    @PutMapping("/with/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id,
                                                 @RequestBody TheaterRequest theaterRequest) {
        Optional<Theater> theaterOptional = theaterService.findById(id);
        if (!theaterOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Theater theater = theaterOptional.get();

        theaterRepository.updateTheaterDetails(theater, theaterRequest,
                seatService, showtimeService);

        Theater updatedTheater = theaterService.save(theater);
        return ResponseEntity.ok(updatedTheater);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
