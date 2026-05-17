package edu.cit.chan.restoradar.feature.admin;

import edu.cit.chan.restoradar.feature.favorite.FavoriteRepository;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantRepository;
import edu.cit.chan.restoradar.feature.review.ReviewEntity;
import edu.cit.chan.restoradar.feature.review.ReviewRepository;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.feature.user.UserRepository;
import edu.cit.chan.restoradar.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public AdminService(UserRepository userRepository,
                        RestaurantRepository restaurantRepository,
                        ReviewRepository reviewRepository,
                        FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
    }

    // ── Users ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<AdminDto.UserSummary> getAllUsers(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable).map(this::toUserSummary);
    }

    @Transactional
    public AdminDto.UserSummary updateUserRole(UUID userId, String newRole) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(newRole);
        return toUserSummary(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    // ── Restaurants ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<AdminDto.RestaurantSummary> getAllRestaurants(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return restaurantRepository.findAll(pageable).map(this::toRestaurantSummary);
    }

    @Transactional
    public void deleteRestaurant(UUID restaurantId) {
        restaurantRepository.delete(restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found")));
    }

    // ── Reviews ───────────────────────────────────────────────────────────────

    @Transactional
    public void deleteReview(UUID reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        RestaurantEntity restaurant = review.getRestaurant();
        reviewRepository.delete(review);

        // Recalculate rating
        double avg = reviewRepository.findAverageRatingByRestaurantId(restaurant.getId()).orElse(0.0);
        int count = reviewRepository.countByRestaurantId(restaurant.getId());
        restaurant.setAverageRating(Math.round(avg * 10.0) / 10.0);
        restaurant.setReviewCount(count);
        restaurantRepository.save(restaurant);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AdminDto.UserSummary toUserSummary(UserEntity user) {
        return AdminDto.UserSummary.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .joinDate(user.getCreatedAt())
                .reviewCount(reviewRepository.countByUserId(user.getId()))
                .favoriteCount(favoriteRepository.countByUserId(user.getId()))
                .build();
    }

    private AdminDto.RestaurantSummary toRestaurantSummary(RestaurantEntity r) {
        return AdminDto.RestaurantSummary.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .averageRating(r.getAverageRating())
                .reviewCount(r.getReviewCount())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
