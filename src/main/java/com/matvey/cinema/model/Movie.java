package com.matvey.cinema.model;

public class Movie {
    private final Long id; // Уникальный идентификатор фильма
    private final String title; // Название фильма
    private final String director; // Режиссер
    private final int releaseYear; // Год выпуска
    private final String genre; // Жанр

    public Movie(Long id, String title, String director, int releaseYear, String genre) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

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
}
