package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<Review> findById(Long id);

    List<Review> findAll();

    List<Review> findReviewsByContent(String content);

    List<Review> findReviewsByMovieTitle(String movieTitle);

    List<Review> findReviewsByUserUsername(String userUsername);

    Review save(Review review);

    void deleteById(Long id);
}