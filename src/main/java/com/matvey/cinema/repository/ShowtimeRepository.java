package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.ShowtimeRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.TheaterService;
import com.matvey.cinema.service.TicketService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    default void updateShowtimeDetails(Showtime showtime, ShowtimeRequest showtimeRequest,
                                       MovieService movieService, TheaterService theaterService,
                                       TicketService ticketService) {
        // Обновляем данные сеанса
        showtime.setDateTime(showtimeRequest.getDateTime());
        showtime.setType(showtimeRequest.getType());

        // Получаем существующие билеты по ID
        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : showtimeRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add); // Добавляем билет, если он найден
        }
        showtime.setTickets(tickets);

        // Найти фильм по ID
        Movie movie = movieService.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + showtimeRequest.getMovieId()));

        // Убедиться, что сеанс связан с фильмом
        if (!movie.getShowtimes().contains(showtime)) {
            movie.getShowtimes().add(showtime);
        }

        // Найти театр по ID
        Theater theater = theaterService.findById(showtimeRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + showtimeRequest.getTheaterId()));

        // Убедиться, что сеанс связан с театром
        if (!theater.getShowtimes().contains(showtime)) {
            theater.getShowtimes().add(showtime);
        }

        // Сохранить изменения
        movieService.save(movie);
        theaterService.save(theater);
    }
}
