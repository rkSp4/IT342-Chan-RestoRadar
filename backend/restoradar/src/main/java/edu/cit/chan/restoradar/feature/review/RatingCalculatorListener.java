package edu.cit.chan.restoradar.feature.review;

import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantRepository;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer that recalculates a restaurant's average rating 
 * and increments its review count when a ReviewCreatedEvent occurs.
 */
@Component
public class RatingCalculatorListener {

    private final RestaurantRepository restaurantRepository;

    public RatingCalculatorListener(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @EventListener
    public void handleReviewCreated(ReviewCreatedEvent event) {
        ReviewEntity createdReview = event.getReview();
        RestaurantEntity restaurant = createdReview.getRestaurant();
        
        System.out.println("OBSERVER TRIGGERED: Recalculating rating for restaurant " + restaurant.getName());
        
        // Atomically increment review count (using mock logic)
        int currentCount = restaurant.getReviewCount() == null ? 0 : restaurant.getReviewCount();
        double currentAverage = restaurant.getAverageRating() == null ? 0.0 : restaurant.getAverageRating();
        
        // Simple moving average calculation logic
        double newTotal = (currentAverage * currentCount) + createdReview.getScore();
        int newCount = currentCount + 1;
        double newAverage = newTotal / newCount;
        
        restaurant.setReviewCount(newCount);
        restaurant.setAverageRating(newAverage);
        
        // Save it back to DB
        restaurantRepository.save(restaurant);
    }
}
