package edu.cit.chan.restoradar.strategy;

import edu.cit.chan.restoradar.entity.RestaurantEntity;
import java.util.List;

public interface SortStrategy {
    void sort(List<RestaurantEntity> restaurants);
}
