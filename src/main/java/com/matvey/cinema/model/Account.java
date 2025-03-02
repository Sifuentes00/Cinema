package com.matvey.cinema.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // Логин пользователя
    private String password; // Пароль (в реальном приложении должен быть зашифрован)

    @ManyToMany
    @JoinTable(
            name = "account_movie", // Название таблицы для связи ManyToMany
            joinColumns = @JoinColumn(name = "account_id"), // Внешний ключ для Account
            inverseJoinColumns = @JoinColumn(name = "movie_id") // Внешний ключ для Movie
    )
    private List<Movie> watchedMovies = new ArrayList<>(); // Просмотренные фильмы

    // Конструкторы, геттеры и сеттеры
    public Account() {
    }

    public Account(String username, String password, User user) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(List<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }
}
