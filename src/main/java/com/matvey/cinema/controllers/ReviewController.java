package com.matvey.cinema.controllers;

import com.matvey.cinema.model.dto.ReviewRequest;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.repository.ReviewRepository;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.ReviewService;
import com.matvey.cinema.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Review Controller", description = "API для управления отзывами")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    public ReviewController(ReviewService reviewService, MovieService movieService,
                            UserService userService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы",
            description = "Возвращает список всех отзывов в базе данных")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список отзывов успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class)))
    })
    public ResponseEntity<List<Review>> getAllReviews() {
        logger.debug("Запрос на получение всех отзывов");
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID", description = "Возвращает отзыв с указанным ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Отзыв успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
        @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
    })
    public ResponseEntity<Review> getReviewById(
            @Parameter(description = "Идентификатор отзыва", example = "1") @PathVariable Long id) {
        logger.debug("Запрос на получение отзыва с ID: {}", id);
        Optional<Review> review = reviewService.findById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.error("Отзыв с ID {} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/movie")
    @Operation(summary = "Получить отзывы по названию фильма",
            description = "Возвращает список отзывов для указанного названия фильма")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список отзывов успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
        @ApiResponse(responseCode = "204", description = "Отзывы не найдены", content = @Content)
    })
    public ResponseEntity<List<Review>> getReviewsByMovieTitle(
            @RequestParam String movieTitle) {
        logger.debug("Запрос на получение отзывов для фильма");
        List<Review> reviews = reviewService.findReviewsByMovieTitle(movieTitle);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user")
    @Operation(summary = "Получить отзывы по имени пользователя",
            description = "Возвращает список отзывов для указанного имени пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список отзывов успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
        @ApiResponse(responseCode = "204", description = "Отзывы не найдены", content = @Content)
    })
    public ResponseEntity<List<Review>> getReviewsByUserUsername(
            @RequestParam String userUsername) {
        logger.debug("Запрос на получение отзывов для пользователя");
        List<Review> reviews = reviewService.findReviewsByUserUsername(userUsername);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/content")
    @Operation(summary = "Получить отзывы по содержимому",
            description = "Возвращает список отзывов, содержащих указанный текст")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список отзывов успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
        @ApiResponse(responseCode = "204", description = "Отзывы не найдены", content = @Content)
    })
    public ResponseEntity<List<Review>> getReviewsByContent(
            @RequestParam String content) {
        logger.debug("Запрос на получение отзывов, содержащих текст");
        List<Review> reviews = reviewService.findReviewsByContent(content);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв",
            description = "Создает новый отзыв на основе предоставленных данных")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                description = "Отзыв успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
        @ApiResponse(responseCode = "400",
                description = "Неверные входные данные", content = @Content)
    })
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        logger.debug("Запрос на создание нового отзыва: {}", reviewRequest);
        Review review = new Review();
        reviewRepository.updateReviewDetails(review, reviewRequest, movieService, userService);
        Review createdReview = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв",
            description = "Обновляет существующий отзыв с указанным ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
        @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
    })
    public ResponseEntity<Review> updateReview(
            @Parameter(description = "Идентификатор отзыва для обновления",
                    example = "1") @PathVariable Long id,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        logger.debug("Запрос на обновление отзыва с ID: {}", id);
        Optional<Review> existingReview = reviewService.findById(id);
        if (existingReview.isPresent()) {
            reviewRepository.updateReviewDetails(existingReview.get(), reviewRequest,
                    movieService, userService);
            Review updatedReview = reviewRepository.save(existingReview.get());
            return ResponseEntity.ok(updatedReview);
        } else {
            logger.error("Отзыв с ID {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв", description = "Удаляет отзыв с указанным ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
                    description = "Отзыв успешно удален", content = @Content),
        @ApiResponse(responseCode = "404",
                    description = "Отзыв не найден", content = @Content)
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Идентификатор отзыва для удаления", example = "1")
            @PathVariable Long id) {
        logger.debug("Запрос на удаление отзыва с ID: {}", id);
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
