package com.matvey.cinema.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = false)
public class TicketRequest {

    @Positive(message = "Поле 'price' должно быть положительным числом")
    private double price;

    @NotNull(message = "Поле 'showtimeId' не должно быть пустым")
    private Long showtimeId;

    @NotNull(message = "Поле 'userId' не должно быть пустым")
    private Long userId;

    @NotNull(message = "Поле 'seatId' не должно быть пустым")
    private Long seatId;

    // Конструктор без параметров
    public TicketRequest() {
    }

    // Конструктор с параметрами
    public TicketRequest(double price, Long showtimeId, Long userId, Long seatId) {
        this.price = price;
        this.showtimeId = showtimeId;
        this.userId = userId;
        this.seatId = seatId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }
}