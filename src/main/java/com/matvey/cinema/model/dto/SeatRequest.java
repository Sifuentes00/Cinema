package com.matvey.cinema.model.dto;

import java.util.List;

public class SeatRequest {
    private int seatRow;
    private int number;
    private boolean isAvailable;
    private Long theaterId;
    private List<Long> ticketIds;

    public int getSeatRow() {
        return seatRow;
    }

    public int getNumber() {
        return number;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public List<Long> getTicketIds() {
        return ticketIds;
    }
}