package com.matvey.cinema.model.dto;

import java.util.List;

public class TheaterRequest {
    private String name;
    private int capacity;
    private List<Long> seatIds;
    private List<Long> showtimeIds;

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public List<Long> getShowtimeIds() {
        return showtimeIds;
    }

    public void setShowtimeIds(List<Long> showtimeIds) {
        this.showtimeIds = showtimeIds;
    }
}