package edu.cit.chan.restoradar.feature.restaurant;

import java.util.List;

public interface SortStrategy {
    void sort(List<RestaurantEntity> restaurants);
}
