package com.matvey.cinema.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = false)
public class ReviewRequest {

    @NotBlank(message = "Поле 'content' не должно быть пустым")
    private String content;

    @NotNull(message = "Поле 'movieId' не должно быть пустым")
    private Long movieId;

    @NotNull(message = "Поле 'userId' не должно быть пустым")
    private Long userId;

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
