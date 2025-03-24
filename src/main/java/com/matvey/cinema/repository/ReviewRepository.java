package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.ReviewRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.service.MovieService;
import com.matvey.cinema.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // JPQL: Найти отзывы по фильму
    @Query("SELECT r FROM Review r WHERE r.content = :content")
    List<Review> findReviewsByContent(@Param("content") String content);

    // Поиск отзывов по названию фильма
    @Query(value =
            "SELECT r.* FROM reviews r JOIN movies m ON r.movie_id = m.id" + " WHERE m.title = ?1",
            nativeQuery = true)
    List<Review> findReviewsByMovieTitle(String movieTitle);

    // Поиск отзывов по имени пользователя
    @Query(value =
            "SELECT r.* FROM reviews r JOIN users u ON r.user_id = u.id" + " WHERE u.username = ?1",
            nativeQuery = true)
    List<Review> findReviewsByUserUsername(String username);

    @Query(value = "SELECT movie_id FROM reviews WHERE id = :id", nativeQuery = true)
    Optional<Long> findMovieIdById(@Param("id") Long id);

    @Query(value = "SELECT user_id FROM reviews WHERE id = :id", nativeQuery = true)
    Optional<Long> findUserIdById(@Param("id") Long id);

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
