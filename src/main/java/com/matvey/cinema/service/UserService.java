package com.matvey.cinema.service;

import com.matvey.cinema.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);

    List<User> findAll();

    User save(User user);

    void deleteById(Long id);
}
