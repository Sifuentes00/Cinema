package com.matvey.cinema.service;

import com.matvey.cinema.model.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<Review> findById(Long id);

    List<Review> findAll();

    Review save(Review review);

    void deleteById(Long id);
}
