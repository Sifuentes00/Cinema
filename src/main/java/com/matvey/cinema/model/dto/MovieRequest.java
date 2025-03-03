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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Long> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(List<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }

    public List<Long> getShowtimeIds() {
        return showtimeIds;
    }

    public void setShowtimeIds(List<Long> showtimeIds) {
        this.showtimeIds = showtimeIds;
    }
}