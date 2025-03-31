package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.model.entities.Review;
import com.matvey.cinema.repository.ReviewRepository;
import com.matvey.cinema.service.ReviewService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

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
            logger.info("Данные отозваны из кэша для ключа: {}", cacheKey);
            return Optional.of((Review) cachedData.get());
        }

        Optional<Review> review = reviewRepository.findById(id);
        review.ifPresent(value -> {
            cache.put(cacheKey, value);
            logger.info("Данные добавлены в кэш для ключа: {}", cacheKey);
        });

        return review;
    }

    @Override
    public List<Review> findAll() {
        String cacheKey = CacheKeys.REVIEWS_ALL;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Все отзывы отозваны из кэша.");
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findAll();
        cache.put(cacheKey, reviews);
        logger.info("Все отзывы добавлены в кэш.");

        return reviews;
    }

    @Override
    public List<Review> findReviewsByContent(String content) {
        String cacheKey = CacheKeys.REVIEWS_CONTENT_PREFIX + content;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Отзывы по содержимому '{}' отозваны из кэша.", content);
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByContent(content);
        cache.put(cacheKey, reviews);
        logger.info("Отзывы по содержимому '{}' добавлены в кэш.", content);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByMovieTitle(String movieTitle) {
        String cacheKey = CacheKeys.REVIEWS_MOVIE_PREFIX + movieTitle;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Отзывы по фильму '{}' отозваны из кэша.", movieTitle);
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByMovieTitle(movieTitle);
        cache.put(cacheKey, reviews);
        logger.info("Отзывы по фильму '{}' добавлены в кэш.", movieTitle);

        return reviews;
    }

    @Override
    public List<Review> findReviewsByUserUsername(String userUsername) {
        String cacheKey = CacheKeys.REVIEWS_USER_PREFIX + userUsername;

        Optional<Object> cachedData = cache.get(cacheKey);
        if (cachedData.isPresent()) {
            logger.info("Отзывы пользователя '{}' отозваны из кэша.", userUsername);
            return (List<Review>) cachedData.get();
        }

        List<Review> reviews = reviewRepository.findReviewsByUserUsername(userUsername);
        cache.put(cacheKey, reviews);
        logger.info("Отзывы пользователя '{}' добавлены в кэш.", userUsername);

        return reviews;
    }

    @Override
    public Review save(Review review) {
        Review savedReview = reviewRepository.save(review);

        cache.evict(CacheKeys.REVIEWS_ALL);
        cache.evict(CacheKeys.REVIEW_PREFIX + savedReview.getId());
        cache.evict(CacheKeys.REVIEWS_CONTENT_PREFIX + savedReview.getContent());

        Optional<Long> movieIdOpt = reviewRepository.findMovieIdById(savedReview.getId());
        Optional<Long> userIdOpt = reviewRepository.findUserIdById(savedReview.getId());

        movieIdOpt.ifPresent(movieId -> {
            cache.evict(CacheKeys.REVIEWS_MOVIE_PREFIX + movieId);
            logger.info("Кэш для отзывов по фильму с ID '{}' очищен.", movieId);
        });
        userIdOpt.ifPresent(userId -> {
            cache.evict(CacheKeys.REVIEWS_USER_PREFIX + userId);
            logger.info("Кэш для отзывов пользователя с ID '{}' очищен.", userId);
        });

        logger.info("Отзыв с ID '{}' успешно сохранен.", savedReview.getId());
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

            movieIdOpt.ifPresent(movieId -> {
                cache.evict(CacheKeys.REVIEWS_MOVIE_PREFIX + movieId);
                logger.info("Кэш для отзывов по фильму с ID '{}' очищен при удалении отзыва.",
                        movieId);
            });
            userIdOpt.ifPresent(userId -> {
                cache.evict(CacheKeys.REVIEWS_USER_PREFIX + userId);
                logger.info("Кэш для отзывов пользователя с ID '{}' очищен при удалении отзыва.",
                        userId);
            });

            logger.info("Отзыв с ID '{}' успешно удален.", review.getId());
        });
        reviewRepository.deleteById(id);
    }
}

