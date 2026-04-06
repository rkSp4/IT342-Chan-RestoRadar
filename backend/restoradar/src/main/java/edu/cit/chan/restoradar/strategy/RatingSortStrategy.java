package edu.cit.chan.restoradar.strategy;

import edu.cit.chan.restoradar.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("top-rated")
public class RatingSortStrategy implements SortStrategy {
    @Override
    public void sort(List<RestaurantEntity> restaurants) {
        restaurants.sort((r1, r2) -> {
            double rating1 = r1.getAverageRating() != null ? r1.getAverageRating() : 0.0;
            double rating2 = r2.getAverageRating() != null ? r2.getAverageRating() : 0.0;
            return Double.compare(rating2, rating1); // Descending
        });
    }
}
