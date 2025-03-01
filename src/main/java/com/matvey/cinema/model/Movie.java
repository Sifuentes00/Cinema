package com.matvey.cinema.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies") // Указывает, что эта сущность соответствует таблице "movies"
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения id
    private Long id; // Уникальный идентификатор фильма

    @Column(name = "title", nullable = false)
    private String title; // Название фильма

    @Column(name = "director", nullable = false)
    private String director; // Режиссер

    @Column(name = "release_year", nullable = false)
    private int releaseYear; // Год выпуска

    @Column(name = "genre", nullable = false)
    private String genre; // Жанр

    // Пустой конструктор необходим для JPA
    public Movie() {
    }

    public Movie(String title, String director, int releaseYear, String genre) {
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

    // Сеттеры
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
