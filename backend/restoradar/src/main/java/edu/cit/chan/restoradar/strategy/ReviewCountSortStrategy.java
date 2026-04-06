package edu.cit.chan.restoradar.strategy;

import edu.cit.chan.restoradar.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("most-reviewed")
public class ReviewCountSortStrategy implements SortStrategy {
    @Override
    public void sort(List<RestaurantEntity> restaurants) {
        restaurants.sort((r1, r2) -> {
            int count1 = r1.getReviewCount() != null ? r1.getReviewCount() : 0;
            int count2 = r2.getReviewCount() != null ? r2.getReviewCount() : 0;
            return Integer.compare(count2, count1); // Descending
        });
    }
}
