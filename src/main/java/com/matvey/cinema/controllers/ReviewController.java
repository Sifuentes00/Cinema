package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.ReviewRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.model.entities.User;
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

    public ReviewController(ReviewService reviewService, MovieService movieService,
                            UserService userService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.userService = userService;
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
        // Найти фильм по ID
        Movie movie = movieService.findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + reviewRequest.getMovieId()));

        // Найти пользователя по ID
        User user = userService.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: "
                        + reviewRequest.getUserId()));

        // Создать новый отзыв
        Review review = new Review();
        review.setContent(reviewRequest.getContent());

        // Добавить отзыв в список отзывов пользователя
        user.getReviews().add(review);

        // Добавить отзыв в список отзывов фильма
        movie.getReviews().add(review);

        // Сохранить пользователя (каскадно сохранит отзыв)
        userService.save(user);

        // Сохранить фильм
        movieService.save(movie);

        return ResponseEntity.ok(review);
    }

    // Новый метод для создания отзыва без указания ID фильма
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
            @PathVariable Long id, // ID отзыва, который нужно обновить
            @RequestBody ReviewRequest reviewRequest) {

        // Найти существующий отзыв по ID
        Review existingReview = reviewService.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден с ID: " + id));

        // Найти фильм по ID
        Movie movie = movieService.findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + reviewRequest.getMovieId()));

        // Найти пользователя по ID
        User user = userService.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: "
                        + reviewRequest.getUserId()));

        // Обновить содержимое отзыва
        existingReview.setContent(reviewRequest.getContent());

        // Убедиться, что отзыв связан с пользователем
        if (!user.getReviews().contains(existingReview)) {
            user.getReviews().add(existingReview);
        }

        // Убедиться, что отзыв связан с фильмом
        if (!movie.getReviews().contains(existingReview)) {
            movie.getReviews().add(existingReview);
        }

        // Сохранить пользователя (каскадно сохранит отзыв)
        userService.save(user);

        // Сохранить фильм
        movieService.save(movie);

        // Возвращаем обновленный отзыв
        return ResponseEntity.ok(existingReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
