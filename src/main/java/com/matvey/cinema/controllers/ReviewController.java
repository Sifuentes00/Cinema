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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.findById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/movie")
    public ResponseEntity<List<Review>> getReviewsByMovieTitle(@RequestParam String movieTitle) {
        List<Review> reviews = reviewService.findReviewsByMovieTitle(movieTitle);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Review>> getReviewsByUserUsername(@RequestParam String
                                                                             userUsername) {
        List<Review> reviews = reviewService.findReviewsByUserUsername(userUsername);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/content")
    public ResponseEntity<List<Review>> getReviewsByContent(@RequestParam String content) {
        List<Review> reviews = reviewService.findReviewsByContent(content);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = new Review();

        reviewRepository.updateReviewDetails(review, reviewRequest, movieService, userService);

        Review createdReview = reviewRepository.save(review);

        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("{id}")
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
