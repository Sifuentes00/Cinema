package com.matvey.cinema.model.dto;

public class ReviewRequest {
    private String content;
    private Long movieId;
    private Long userId;

    // Геттеры и сеттеры
    public String getContent() {
        return content;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getUserId() {
        return userId;
    }
}