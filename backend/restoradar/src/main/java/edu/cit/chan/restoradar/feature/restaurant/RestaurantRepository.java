package edu.cit.chan.restoradar.feature.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, UUID> {
    // Example method for fetching restaurants roughly by area or specific conditions
    // In a real scenario, this would likely be an advanced geographic point query 
    // or a custom @Query. We define this for the sake of the Facade pattern demo.
    List<RestaurantEntity> findByPriceRange(String priceRange);
}
