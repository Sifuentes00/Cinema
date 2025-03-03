package com.matvey.cinema.model.dto;

import java.util.List;

public class MovieRequest {
    private String title;
    private String director;
    private int releaseYear;
    private String genre;
    private List<Long> reviewIds;
    private List<Long> showtimeIds;

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public List<Long> getReviewIds() {
        return reviewIds;
    }

    public List<Long> getShowtimeIds() {
        return showtimeIds;
    }
}