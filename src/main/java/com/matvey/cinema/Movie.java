package com.matvey.cinema;

public class Movie {
    private String title; // Название фильма
    private String director; // Режиссер
    private int releaseYear; // Год выпуска
    private String genre; // Жанр

    // Конструктор
    public Movie(String title, String director, int releaseYear, String genre) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    // Геттеры
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
