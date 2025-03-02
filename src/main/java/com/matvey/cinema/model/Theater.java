package com.matvey.cinema.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "theaters") // Указывает, что эта сущность соответствует таблице "theaters"
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения id
    private Long id; // Уникальный идентификатор зала

    private String name; // Название зала
    private int capacity; // Вместимость зала

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id") // Внешний ключ в таблице seats
    private List<Seat> seats = new ArrayList<>();

    // Пустой конструктор необходим для JPA
    public Theater() {
    }

    public Theater(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
