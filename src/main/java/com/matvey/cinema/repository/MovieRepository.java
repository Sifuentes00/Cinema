package com.matvey.cinema.repository;

import com.matvey.cinema.model.dto.MovieRequest;
import com.matvey.cinema.model.entities.Movie;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.model.entities.Showtime;
import com.matvey.cinema.service.ReviewService;
import com.matvey.cinema.service.ShowtimeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    default void updateMovieDetails(Movie movie, MovieRequest movieRequest,
                                    ReviewService reviewService, ShowtimeService showtimeService) {
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setGenre(movieRequest.getGenre());

        // Получаем существующие отзывы по ID
        List<Review> reviews = new ArrayList<>();
        for (Long reviewId : movieRequest.getReviewIds()) {
            Optional<Review> reviewOptional = reviewService.findById(reviewId);
            reviewOptional.ifPresent(reviews::add); // Добавляем отзыв, если он найден
        }
        movie.setReviews(reviews);

        // Получаем существующие сеансы по ID
        List<Showtime> showtimes = new ArrayList<>();
        for (Long showtimeId : movieRequest.getShowtimeIds()) {
            Optional<Showtime> showtimeOptional = showtimeService.findById(showtimeId);
            if (showtimeOptional.isPresent()) {
                showtimes.add(showtimeOptional.get());
            } else {
                throw new RuntimeException("Showtime not found with id: " + showtimeId);
            }
        }
        movie.setShowtimes(showtimes);
    }
}
