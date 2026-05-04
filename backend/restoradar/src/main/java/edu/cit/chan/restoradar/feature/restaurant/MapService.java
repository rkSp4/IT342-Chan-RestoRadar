package edu.cit.chan.restoradar.feature.restaurant;

import java.util.List;

public interface MapService {
    /**
     * Gets nearby places within a specific radius of the given latitude and longitude.
     *
     * @param lat    Latitude of the center point
     * @param lng    Longitude of the center point
     * @param radius Radius to search within (e.g., in kilometers or miles)
     * @return List of nearby RestaurantEntities (or locations)
     */
    List<RestaurantEntity> getNearbyPlaces(double lat, double lng, double radius);
}
