package edu.cit.chan.restoradar.service;

import edu.cit.chan.restoradar.dto.AdminDto;
import edu.cit.chan.restoradar.entity.Restaurant;
import edu.cit.chan.restoradar.entity.Review;
import edu.cit.chan.restoradar.entity.UserEntity;
import edu.cit.chan.restoradar.exception.ResourceNotFoundException;
import edu.cit.chan.restoradar.repository.FavoriteRepository;
import edu.cit.chan.restoradar.repository.RestaurantRepository;
import edu.cit.chan.restoradar.repository.ReviewRepository;
import edu.cit.chan.restoradar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    // ── GET /admin/users ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<AdminDto.UserSummary> getAllUsers(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable).map(this::toUserSummary);
    }

    // ── PUT /admin/users/{id}/role ────────────────────────────────────────────

    @Transactional
    public AdminDto.UserSummary updateUserRole(UUID userId, String newRole) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(newRole);
        return toUserSummary(userRepository.save(user));
    }

    // ── DELETE /admin/users/{id} ──────────────────────────────────────────────

    @Transactional
    public void deleteUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    // ── GET /admin/restaurants/pending ────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<AdminDto.PendingRestaurant> getPendingRestaurants(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return restaurantRepository.findByApprovedFalse(pageable).map(this::toPendingRestaurant);
    }

    // ── PUT /admin/restaurants/{id}/approve ───────────────────────────────────

    @Transactional
    public AdminDto.PendingRestaurant approveRestaurant(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        restaurant.setApproved(true);
        return toPendingRestaurant(restaurantRepository.save(restaurant));
    }

    // ── DELETE /admin/reviews/{id} ────────────────────────────────────────────

    @Transactional
    public void deleteReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        Restaurant restaurant = review.getRestaurant();
        reviewRepository.delete(review);

        // Recalculate rating after admin removes a review
        double avg = reviewRepository.findAverageRatingByRestaurantId(restaurant.getId())
                .orElse(0.0);
        int count = reviewRepository.countByRestaurantId(restaurant.getId());
        restaurant.setRating(Math.round(avg * 10.0) / 10.0);
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

    private AdminDto.PendingRestaurant toPendingRestaurant(Restaurant r) {
        return AdminDto.PendingRestaurant.builder()
                .id(r.getId())
                .name(r.getName())
                .ownerId(r.getOwner() != null ? r.getOwner().getId() : null)
                .ownerName(r.getOwner() != null ? r.getOwner().getFullName() : null)
                .submittedAt(r.getCreatedAt())
                .status(r.getApproved() ? "APPROVED" : "PENDING")
                .build();
    }
}