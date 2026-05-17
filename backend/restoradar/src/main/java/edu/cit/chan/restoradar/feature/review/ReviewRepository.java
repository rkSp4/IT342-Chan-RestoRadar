package edu.cit.chan.restoradar.feature.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.restaurant.id = :restaurantId")
        Optional<Double> findAverageRatingByRestaurantId(@Param("restaurantId") UUID restaurantId);

        int countByRestaurantId(UUID restaurantId);
        int countByUserId(UUID userId);
}
