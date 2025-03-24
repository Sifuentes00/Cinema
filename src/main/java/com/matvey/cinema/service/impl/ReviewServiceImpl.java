package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.repository.ReviewRepository;
import com.matvey.cinema.service.ReviewService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final InMemoryCache cache;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, InMemoryCache cache) {
        this.reviewRepository = reviewRepository;
        this.cache = cache;
    }

    @Override
    public Optional<Review> findById(Long id) {
        String cacheKey = "review_" + id;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return Optional.of((Review) cachedData.get());
        }

        Optional<Review> review = reviewRepository.findById(id);

        review.ifPresent(value -> cache.put(cacheKey, value));

        return review;
    }


    @Override
    public List<Review> findAll() {
        String cacheKey = "reviews_all";

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findAll();

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByContent(String content) {
        String cacheKey = "reviews_content_" + content;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByContent(content);

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByMovieId(Long movieId) {
        String cacheKey = "reviews_movie_" + movieId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByMovieId(movieId);

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByUserId(Long userId) {
        String cacheKey = "reviews_user_" + userId;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public Review save(Review review) {
        Review savedReview = reviewRepository.save(review);

        // Очищаем кэш для всех отзывов и для конкретного отзыва
        cache.evict("reviews_all");
        cache.evict("review_" + savedReview.getId());
        cache.evict("reviews_content_" + savedReview.getContent());

        Optional<Long> movieIdOpt = reviewRepository.findMovieIdById(savedReview.getId());
        Optional<Long> userIdOpt = reviewRepository.findUserIdById(savedReview.getId());

        movieIdOpt.ifPresent(movieId -> cache.evict("reviews_movie_" + movieId));
        userIdOpt.ifPresent(userId -> cache.evict("reviews_user_" + userId));

        return savedReview;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        reviewOpt.ifPresent(review -> {
            cache.evict("reviews_all");
            cache.evict("review_" + review.getId());
            cache.evict("reviews_content_" + review.getContent());

            Optional<Long> movieIdOpt = reviewRepository.findMovieIdById(review.getId());
            Optional<Long> userIdOpt = reviewRepository.findUserIdById(review.getId());

            movieIdOpt.ifPresent(movieId -> cache.evict("reviews_movie_" + movieId));
            userIdOpt.ifPresent(userId -> cache.evict("reviews_user_" + userId));
        });
        reviewRepository.deleteById(id);
    }
}