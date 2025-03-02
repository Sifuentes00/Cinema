package com.matvey.cinema.repository;

import com.matvey.cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Здесь можно добавлять дополнительные методы, если нужно
}
