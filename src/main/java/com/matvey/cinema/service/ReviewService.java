package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<Review> findById(Long id);

    List<Review> findAll();

    List<Review> findReviewsByContent(String content);

    List<Review> findReviewsByMovieId(Long movieId);

    List<Review> findReviewsByUserId(Long userId);

    Review save(Review review);

    void deleteById(Long id);
}