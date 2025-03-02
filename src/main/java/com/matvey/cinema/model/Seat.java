package com.matvey.cinema.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats") // Указывает, что эта сущность соответствует таблице "seats"
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения id
    private Long id; // Уникальный идентификатор места

    private int row; // Номер ряда
    private int number; // Номер места
    private boolean isAvailable; // Доступно ли место

    // Пустой конструктор необходим для JPA
    public Seat() {
    }

    public Seat(int row, int number, boolean isAvailable) {
        this.row = row;
        this.number = number;
        this.isAvailable = isAvailable;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
