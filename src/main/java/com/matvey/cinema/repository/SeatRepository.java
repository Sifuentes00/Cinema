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
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    default void updateSeatDetails(Seat seat, SeatRequest seatRequest,
                                   TheaterService theaterService, TicketService ticketService) {
        // Обновляем данные места
        seat.setSeatRow(seatRequest.getSeatRow());
        seat.setNumber(seatRequest.getNumber());
        seat.setAvailable(seatRequest.isAvailable());

        // Найти театр по ID
        Theater theater = theaterService.findById(seatRequest.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Театр не найден с ID: "
                        + seatRequest.getTheaterId()));

        // Установить связь с театром
        if (!theater.getSeats().contains(seat)) {
            theater.getSeats().add(seat);
        }

        // Получить существующие билеты по ID
        List<Ticket> tickets = new ArrayList<>();
        for (Long ticketId : seatRequest.getTicketIds()) {
            Optional<Ticket> ticketOptional = ticketService.findById(ticketId);
            ticketOptional.ifPresent(tickets::add); // Добавляем билет, если он найден
        }
        seat.setTickets(tickets);


    }
}
