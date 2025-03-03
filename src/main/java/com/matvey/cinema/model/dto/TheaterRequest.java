package com.matvey.cinema.model.dto;

import java.util.List;

public class TheaterRequest {
    private String name;
    private int capacity;
    private List<Long> seatIds;
    private List<Long> showtimeIds;

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public List<Long> getShowtimeIds() {
        return showtimeIds;
    }
}