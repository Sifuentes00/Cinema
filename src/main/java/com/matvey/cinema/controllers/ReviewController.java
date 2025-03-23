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

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Review>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<Review> reviews = reviewRepository.findReviewsByMovieId(movieId);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращает 204, если отзывов нет
        }
        return ResponseEntity.ok(reviews); // Возвращает 200 и список отзывов
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращает 204, если отзывов нет
        }
        return ResponseEntity.ok(reviews); // Возвращает 200 и список отзывов
    }

    @GetMapping("/content")
    public ResponseEntity<List<Review>> getReviewsByContent(@RequestParam String content) {
        List<Review> reviews = reviewRepository.findReviewsByContent(content);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращает 204, если отзывов нет
        }
        return ResponseEntity.ok(reviews); // Возвращает 200 и список отзывов
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
