package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.ReviewRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    default void updateReviewDetails(Review review, ReviewRequest reviewRequest,
                                     MovieService movieService, UserService userService) {
        // Обновляем содержимое отзыва
        review.setContent(reviewRequest.getContent());

        // Получаем фильм по ID
        Movie movie = movieService.findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + reviewRequest.getMovieId()));

        // Получаем пользователя по ID
        User user = userService.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: "
                        + reviewRequest.getUserId()));

        // Ассоциируем отзыв с фильмом и пользователем
        if (!user.getReviews().contains(review)) {
            user.getReviews().add(review);
        }
        if (!movie.getReviews().contains(review)) {
            movie.getReviews().add(review);
        }

        // Сохраняем изменения
        userService.save(user);
        movieService.save(movie);
    }

}
