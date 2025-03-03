package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.TheaterRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.service.SeatService;
import com.matvey.cinema.service.ShowtimeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    default void updateTheaterDetails(Theater theater, TheaterRequest theaterRequest,
                                      SeatService seatService, ShowtimeService showtimeService) {
        // Обновляем данные театра
        theater.setName(theaterRequest.getName());
        theater.setCapacity(theaterRequest.getCapacity());

        // Получаем существующие места по их ID
        List<Seat> seats = new ArrayList<>();
        for (Long seatId : theaterRequest.getSeatIds()) {
            Optional<Seat> seatOptional = seatService.findById(seatId);
            if (seatOptional.isPresent()) {
                seats.add(seatOptional.get());
            } else {
                throw new RuntimeException("Место не найдено с ID: " + seatId);
            }
        }
        theater.setSeats(seats);

        // Получаем существующие сеансы по их ID
        List<Showtime> showtimes = new ArrayList<>();
        for (Long showtimeId : theaterRequest.getShowtimeIds()) {
            Optional<Showtime> showtimeOptional = showtimeService.findById(showtimeId);
            if (showtimeOptional.isPresent()) {
                showtimes.add(showtimeOptional.get());
            } else {
                throw new RuntimeException("Сеанс не найден с ID: " + showtimeId);
            }
        }
        theater.setShowtimes(showtimes);
    }


}
