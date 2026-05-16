package edu.cit.chan.restoradar.service;

import edu.cit.chan.restoradar.dto.ReviewDto;
import edu.cit.chan.restoradar.entity.Restaurant;
import edu.cit.chan.restoradar.entity.Review;
import edu.cit.chan.restoradar.entity.UserEntity;
import edu.cit.chan.restoradar.exception.ForbiddenException;
import edu.cit.chan.restoradar.exception.ResourceNotFoundException;
import edu.cit.chan.restoradar.repository.RestaurantRepository;
import edu.cit.chan.restoradar.repository.ReviewRepository;
import edu.cit.chan.restoradar.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // ── GET /restaurants/{id}/reviews ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ReviewDto.Response> getReviewsForRestaurant(
            UUID restaurantId, String sort, int page, int limit) {

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }

        Sort sortOrder = switch (sort) {
            case "highest" -> Sort.by("rating").descending();
            case "lowest"  -> Sort.by("rating").ascending();
            default        -> Sort.by("createdAt").descending(); // "recent"
        };

        Pageable pageable = PageRequest.of(page - 1, limit, sortOrder);
        return reviewRepository.findByRestaurantId(restaurantId, pageable)
                .map(this::toResponse);
    }

    // ── POST /restaurants/{id}/reviews ────────────────────────────────────────

    @Transactional
    public ReviewDto.Response addReview(UUID restaurantId, ReviewDto.Request req, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (reviewRepository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new ForbiddenException("You have already reviewed this restaurant");
        }

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        review.setPhotos(req.getPhotos() != null ? req.getPhotos() : new ArrayList<>());

        Review saved = reviewRepository.save(review);
        recalculateRating(restaurant);

        return toResponse(saved);
    }

    // ── PUT /reviews/{id} ─────────────────────────────────────────────────────

    @Transactional
    public ReviewDto.Response updateReview(UUID reviewId, ReviewDto.Request req, UUID userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own reviews");
        }

        if (req.getRating() != null) review.setRating(req.getRating());
        if (req.getComment() != null) review.setComment(req.getComment());
        if (req.getPhotos() != null) review.setPhotos(req.getPhotos());

        Review saved = reviewRepository.save(review);
        recalculateRating(review.getRestaurant());

        return toResponse(saved);
    }

    // ── DELETE /reviews/{id} ──────────────────────────────────────────────────

    @Transactional
    public void deleteReview(UUID reviewId, UUID userId, String userRole) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        boolean isOwner = review.getUser().getId().equals(userId);
        boolean isAdmin = "ADMIN".equals(userRole);

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You can only delete your own reviews");
        }

        Restaurant restaurant = review.getRestaurant();
        reviewRepository.delete(review);
        recalculateRating(restaurant);
    }

    // ── GET /users/{id}/reviews ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ReviewDto.Response> getReviewsByUser(UUID userId, int page, int limit) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return reviewRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    // Recalculates and persists the restaurant's average rating + review count
    // after any change to its reviews.
    private void recalculateRating(Restaurant restaurant) {
        double avg = reviewRepository.findAverageRatingByRestaurantId(restaurant.getId())
                .orElse(0.0);
        int count = reviewRepository.countByRestaurantId(restaurant.getId());

        // Round to 1 decimal place
        restaurant.setRating(Math.round(avg * 10.0) / 10.0);
        restaurant.setReviewCount(count);
        restaurantRepository.save(restaurant);
    }

    private ReviewDto.Response toResponse(Review r) {
        ReviewDto.Response res = new ReviewDto.Response();
        res.setId(r.getId());
        res.setUserId(r.getUser().getId());
        res.setUserName(r.getUser().getFullName());
        res.setRating(r.getRating());
        res.setComment(r.getComment());
        res.setPhotos(r.getPhotos());
        res.setCreatedAt(r.getCreatedAt());
        res.setUpdatedAt(r.getUpdatedAt());
        return res;
    }
}