package edu.cit.chan.restoradar.feature.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {

    // All reviews for a restaurant, sorted by the chosen strategy
    Page<ReviewEntity> findByRestaurantId(UUID restaurantId, Pageable pageable);

    // All reviews written by a user
    Page<ReviewEntity> findByUserId(UUID userId, Pageable pageable);

    // Check if a user already reviewed a restaurant (one review per user per restaurant)
    boolean existsByUserIdAndRestaurantId(UUID userId, UUID restaurantId);

    // Used to recalculate restaurant average rating after add/edit/delete
    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.restaurant.id = :restaurantId")
    Optional<Double> findAverageRatingByRestaurantId(@Param("restaurantId") UUID restaurantId);

    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.restaurant.id = :restaurantId")
    int countByRestaurantId(@Param("restaurantId") UUID restaurantId);

    int countByUserId(UUID userId);
}