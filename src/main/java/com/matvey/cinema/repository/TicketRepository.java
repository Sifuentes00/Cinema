package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.TicketRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.service.SeatService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Поиск билетов по имени пользователя
    @Query(value =
            "SELECT t.* FROM tickets t JOIN users u ON t.user_id = u.id WHERE u.username = ?1",
            nativeQuery = true)
    List<Ticket> findTicketsByUserUsername(String username);

    // Поиск билетов по дате и времени сеанса
    @Query(value =
            "SELECT t.* FROM tickets t JOIN showtimes s ON t.showtime_id=s.id WHERE s.date_time=?1",
            nativeQuery = true)
    List<Ticket> findTicketsByShowtimeDateTime(String showtimeDateTime);

    @Query(value = "SELECT * FROM tickets WHERE seat_id = ?1", nativeQuery = true)
    List<Ticket> findTicketsBySeatId(Long seatId);

    @Query(value = "SELECT user_id FROM tickets WHERE id = :id", nativeQuery = true)
    Optional<Long> findUserIdById(@Param("id") Long id);

    @Query(value = "SELECT showtime_id FROM tickets WHERE id = :id", nativeQuery = true)
    Optional<Long> findShowtimeIdById(@Param("id") Long id);

    @Query(value = "SELECT seat_id FROM tickets WHERE id = :id", nativeQuery = true)
    Optional<Long> findSeatIdById(@Param("id") Long id);

    default void updateTicketDetails(Ticket ticket, TicketRequest ticketRequest,
                                     ShowtimeService showtimeService, SeatService seatService,
                                     UserService userService) {
        Showtime showtime = showtimeService.findById(ticketRequest.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: "
                        + ticketRequest.getShowtimeId()));
        Seat seat = seatService.findById(ticketRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with id: "
                        + ticketRequest.getSeatId()));

        ticket.setPrice(ticketRequest.getPrice());

        User user = userService.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: "
                        + ticketRequest.getUserId()));
        showtime.getTickets().add(ticket);
        seat.getTickets().add(ticket);
        user.getTickets().add(ticket);
    }
}
