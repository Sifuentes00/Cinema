package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.SeatRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.model.entities.Theater;
import com.matvey.cinema.model.entities.Ticket;
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
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "SELECT * FROM seats WHERE theater_id = ?1", nativeQuery = true)
    List<Seat> findSeatsByTheaterId(Long theaterId);

    @Query(value = "SELECT theater_id FROM seats WHERE id = :id", nativeQuery = true)
    Optional<Long> findTheaterIdById(@Param("id") Long id);

    default void updateSeatDetails(Seat seat, SeatRequest seatRequest,
                                   TheaterService theaterService, TicketService ticketService) {
        seat.setSeatRow(seatRequest.getSeatRow());
        seat.setNumber(seatRequest.getNumber());
        seat.setAvailable(seatRequest.isAvailable());

        Theater theater = theaterService.findById(seatRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + seatRequest.getTheaterId()));

        if (!theater.getSeats().contains(seat)) {
            theater.getSeats().add(seat);
        }

        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : seatRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add);
        }
        seat.setTickets(tickets);
    }
}
