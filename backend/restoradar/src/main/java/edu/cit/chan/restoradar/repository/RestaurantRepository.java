package edu.cit.chan.restoradar.repository;

import edu.cit.chan.restoradar.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    // All approved restaurants (paginated)
    Page<Restaurant> findByApprovedTrue(Pageable pageable);

    // Search by name or cuisine (case-insensitive)
    @Query("""
        SELECT r FROM Restaurant r
        WHERE r.approved = true
          AND (LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(r.cuisine) LIKE LOWER(CONCAT('%', :query, '%')))
        """)
    Page<Restaurant> searchByNameOrCuisine(@Param("query") String query, Pageable pageable);

    // Nearby search using the Haversine formula (returns distance in km)
    // Filtered by radius and optional price range
    @Query(value = """
        SELECT r.*,
            (6371 * ACOS(
                COS(RADIANS(:lat)) * COS(RADIANS(r.latitude))
                * COS(RADIANS(r.longitude) - RADIANS(:lng))
                + SIN(RADIANS(:lat)) * SIN(RADIANS(r.latitude))
            )) AS distance
        FROM restaurants r
        WHERE r.approved = true
          AND (:priceRange IS NULL OR r.price_range = :priceRange)
        HAVING distance <= :radius
        ORDER BY distance ASC
        """,
        countQuery = """
        SELECT COUNT(*) FROM restaurants r
        WHERE r.approved = true
          AND (:priceRange IS NULL OR r.price_range = :priceRange)
        """,
        nativeQuery = true)
    Page<Object[]> findNearby(
        @Param("lat") double lat,
        @Param("lng") double lng,
        @Param("radius") double radius,
        @Param("priceRange") String priceRange,
        Pageable pageable
    );

    Page<Restaurant> findByApprovedFalse(Pageable pageable);
}