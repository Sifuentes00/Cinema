package com.matvey.cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews") // Указывает, что эта сущность соответствует таблице "reviews"
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения id
    private Long id; // Уникальный идентификатор отзыва

    private String content; // Содержимое отзыва

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")// Связь многие-к-одному с Movie
    private Movie movie; // Фильм, к которому относится отзыв

    // Пустой конструктор необходим для JPA
    public Review() {
    }

    public Review(String content, Movie movie) {
        this.content = content;
        this.movie = movie;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
