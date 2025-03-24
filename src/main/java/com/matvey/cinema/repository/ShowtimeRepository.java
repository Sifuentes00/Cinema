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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Поиск сеансов по названию театра
    @Query(value =
            "SELECT st.* FROM showtimes st JOIN theaters t ON st.theater_id=t.id WHERE t.name = ?1",
            nativeQuery = true)
    List<Showtime> findShowtimesByTheaterName(String theaterName);

    // Поиск сеансов по названию фильма
    @Query(value =
            "SELECT st.* FROM showtimes st JOIN movies m ON st.movie_id = m.id WHERE m.title = ?1",
            nativeQuery = true)
    List<Showtime> findShowtimesByMovieTitle(String movieTitle);


    @Query(value = "SELECT theater_id FROM showtimes WHERE id = :id", nativeQuery = true)
    Optional<Long> findTheaterIdById(@Param("id") Long id);

    @Query(value = "SELECT movie_id FROM showtimes WHERE id = :id", nativeQuery = true)
    Optional<Long> findMovieIdById(@Param("id") Long id);

    default void updateShowtimeDetails(Showtime showtime, ShowtimeRequest showtimeRequest,
                                       MovieService movieService, TheaterService theaterService,
                                       TicketService ticketService) {
        showtime.setDateTime(showtimeRequest.getDateTime());
        showtime.setType(showtimeRequest.getType());

        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : showtimeRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add);
        }
        showtime.setTickets(tickets);

        Movie movie = movieService.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + showtimeRequest.getMovieId()));

        if (!movie.getShowtimes().contains(showtime)) {
            movie.getShowtimes().add(showtime);
        }

        Theater theater = theaterService.findById(showtimeRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + showtimeRequest.getTheaterId()));

        if (!theater.getShowtimes().contains(showtime)) {
            theater.getShowtimes().add(showtime);
        }
    }
}
