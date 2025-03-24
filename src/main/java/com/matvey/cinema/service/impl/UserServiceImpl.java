package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.repository.UserRepository;
import com.matvey.cinema.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InMemoryCache cache;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, InMemoryCache cache) {
        this.userRepository = userRepository;
        this.cache = cache;
    }

    @Override
    public Optional<User> findById(Long id) {
        String cacheKey = "user_" + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((User) cachedData.get());
        }

        Optional<User> user = userRepository.findById(id);

        user.ifPresent(value -> cache.put(cacheKey, value));

        return user;
    }

    @Override
    public List<User> findAll() {
        String cacheKey = "users_all";

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<User>) cachedData.get();
        }

        List<User> users = userRepository.findAll();

        cache.put(cacheKey, users);

        return users;
    }

    @Override
    public User save(User user) {
        User savedUser = userRepository.save(user);

        cache.evict("users_all");
        cache.evict("user_" + savedUser.getId());

        return savedUser;
    }

    @Override
    public void deleteById(Long id) {
        cache.evict("users_all");
        cache.evict("user_" + id);

        userRepository.deleteById(id);
    }
}