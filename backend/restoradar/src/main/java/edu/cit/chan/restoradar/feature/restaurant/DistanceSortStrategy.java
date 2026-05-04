package edu.cit.chan.restoradar.feature.restaurant;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("nearest")
public class DistanceSortStrategy implements SortStrategy {
    @Override
    public void sort(List<RestaurantEntity> restaurants) {
        restaurants.sort((r1, r2) -> {
            double d1 = r1.getDistance() != null ? r1.getDistance() : Double.MAX_VALUE;
            double d2 = r2.getDistance() != null ? r2.getDistance() : Double.MAX_VALUE;
            return Double.compare(d1, d2); // Ascending
        });
    }
}
