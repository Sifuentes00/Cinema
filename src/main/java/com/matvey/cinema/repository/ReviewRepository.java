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
        review.setContent(reviewRequest.getContent());

        Movie movie = movieService.findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден с ID: "
                        + reviewRequest.getMovieId()));

        User user = userService.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: "
                        + reviewRequest.getUserId()));

        if (!user.getReviews().contains(review)) {
            user.getReviews().add(review);
        }
        if (!movie.getReviews().contains(review)) {
            movie.getReviews().add(review);
        }


    }

}
