package com.matvey.cinema.model.dto;

import java.util.List;

public class ShowtimeRequest {
    private String dateTime;
    private String type;
    private Long movieId;
    private Long theaterId;
    private List<Long> ticketIds;

    public String getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public List<Long> getTicketIds() {
        return ticketIds;
    }
}