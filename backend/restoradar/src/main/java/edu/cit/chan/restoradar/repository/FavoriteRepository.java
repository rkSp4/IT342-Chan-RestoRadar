package edu.cit.chan.restoradar.repository;

import edu.cit.chan.restoradar.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    Page<Favorite> findByUserId(UUID userId, Pageable pageable);

    Optional<Favorite> findByUserIdAndRestaurantId(UUID userId, UUID restaurantId);

    boolean existsByUserIdAndRestaurantId(UUID userId, UUID restaurantId);

    int countByUserId(UUID userId);
}