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
@Table(name = "tickets") // Указывает, что эта сущность соответствует таблице "tickets"
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения id
    private Long id; // Уникальный идентификатор билета

    private double price; // Цена билета

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id") // Внешний ключ на таблицу showtimes
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Внешний ключ на таблицу users
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id") // Внешний ключ на таблицу seats
    private Seat seat;

    // Пустой конструктор необходим для JPA
    public Ticket() {
    }

    public Ticket(double price, Showtime showtime, User user, Seat seat) {
        this.price = price;
        this.showtime = showtime;
        this.user = user;
        this.seat = seat;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
