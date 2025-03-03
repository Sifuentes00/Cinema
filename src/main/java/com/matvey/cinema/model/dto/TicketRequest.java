package com.matvey.cinema.model.dto;

public class TicketRequest {
    private double price;
    private Long showtimeId;
    private Long userId;
    private Long seatId;

    // Геттеры и сеттеры
    public double getPrice() {
        return price;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSeatId() {
        return seatId;
    }
}