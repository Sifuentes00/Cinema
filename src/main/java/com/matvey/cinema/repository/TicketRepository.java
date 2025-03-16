package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.TicketRequest;
import com.matvey.cinema.model.entities.Seat;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.model.entities.Ticket;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.service.SeatService;
import com.matvey.cinema.service.ShowtimeService;
import com.matvey.cinema.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    default void updateTicketDetails(Ticket ticket, TicketRequest ticketRequest,
                                     ShowtimeService showtimeService, SeatService seatService,
                                     UserService userService) {
        // Находим Showtime, Seat и User по их ID
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
