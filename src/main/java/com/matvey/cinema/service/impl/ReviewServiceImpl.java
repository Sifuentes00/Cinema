package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
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
        String cacheKey = CacheKeys.REVIEW_PREFIX + id;

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
        String cacheKey = CacheKeys.REVIEWS_ALL;

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
        String cacheKey = CacheKeys.REVIEWS_CONTENT_PREFIX + content;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByContent(content);

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByMovieTitle(String movieTitle) {
        String cacheKey = CacheKeys.REVIEWS_MOVIE_PREFIX + movieTitle;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByMovieTitle(movieTitle);

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByUserUsername(String userUsername) {
        String cacheKey = CacheKeys.REVIEWS_USER_PREFIX + userUsername;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByUserUsername(userUsername);

        cache.put(cacheKey, reviews);

        return reviews;
    }

    @Override
    public Review save(Review review) {
        Review savedReview = reviewRepository.save(review);

        // Очищаем кэш для всех отзывов и для конкретного отзыва
        cache.evict(CacheKeys.REVIEWS_ALL);
        cache.evict(CacheKeys.REVIEW_PREFIX + savedReview.getId());
        cache.evict(CacheKeys.REVIEWS_CONTENT_PREFIX + savedReview.getContent());

        Optional<Long> movieIdOpt = reviewRepository.findMovieIdById(savedReview.getId());
        Optional<Long> userIdOpt = reviewRepository.findUserIdById(savedReview.getId());

        movieIdOpt.ifPresent(movieId -> cache.evict(CacheKeys.REVIEWS_MOVIE_PREFIX + movieId));
        userIdOpt.ifPresent(userId -> cache.evict(CacheKeys.REVIEWS_USER_PREFIX + userId));

        return savedReview;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        reviewOpt.ifPresent(review -> {
            cache.evict(CacheKeys.REVIEWS_ALL);
            cache.evict(CacheKeys.REVIEW_PREFIX + review.getId());
            cache.evict(CacheKeys.REVIEWS_CONTENT_PREFIX + review.getContent());

            Optional<Long> movieIdOpt = reviewRepository.findMovieIdById(review.getId());
            Optional<Long> userIdOpt = reviewRepository.findUserIdById(review.getId());

            movieIdOpt.ifPresent(movieId -> cache.evict(CacheKeys.REVIEWS_MOVIE_PREFIX + movieId));
            userIdOpt.ifPresent(userId -> cache.evict(CacheKeys.REVIEWS_USER_PREFIX + userId));
        });
        reviewRepository.deleteById(id);
    }
}
