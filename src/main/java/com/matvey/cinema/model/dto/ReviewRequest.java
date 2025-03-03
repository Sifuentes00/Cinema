package com.matvey.cinema.model.dto;

public class ReviewRequest {
    private String content;
    private Long movieId;
    private Long userId;

    // Геттеры и сеттеры
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}