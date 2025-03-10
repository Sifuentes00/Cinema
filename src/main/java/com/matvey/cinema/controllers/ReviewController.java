package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.ReviewRequest;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.repository.ReviewRepository;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ReviewService;
import com.matvey.cinema.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final MovieService movieService;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewService reviewService, MovieService movieService,
                            UserService userService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.findById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/with")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = new Review();

        reviewRepository.updateReviewDetails(review, reviewRequest, movieService, userService);

        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<Review> createReviewWithoutMovieId(@RequestBody Review review) {
        Review createdReview = reviewService.save(review);

        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        review.setId(id);
        Review updatedReview = reviewService.save(review);
        return ResponseEntity.ok(updatedReview);
    }

    @PutMapping("/with/{id}")
    public ResponseEntity<Review> updateReviewWithUserAndMovie(
            @PathVariable Long id,
            @RequestBody ReviewRequest reviewRequest) {

        Review existingReview = reviewService.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден с ID: " + id));

        reviewRepository.updateReviewDetails(existingReview, reviewRequest,
                                                movieService, userService);

        return ResponseEntity.ok(existingReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
