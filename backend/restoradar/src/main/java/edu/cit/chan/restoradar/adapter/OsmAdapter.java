package edu.cit.chan.restoradar.adapter;

import edu.cit.chan.restoradar.entity.RestaurantEntity;
import edu.cit.chan.restoradar.service.MapService;
// import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

// Intentionally left without @Component so that only one MapService bean exists (GoogleMapsAdapter),
// or you could use @Qualifier / @Primary or profiles if both were @Component.
public class OsmAdapter implements MapService {

    // Hypothetical OpenStreetMap XML/JSON API client
    // private final OsmClient osmClient;

    @Override
    public List<RestaurantEntity> getNearbyPlaces(double lat, double lng, double radius) {
        // Translation from OpenStreetMap data to the internal domain entity
        System.out.println("Calling OpenStreetMap API with lat: " + lat + ", lng: " + lng + ", radius: " + radius);
        
        List<RestaurantEntity> restaurants = new ArrayList<>();
        RestaurantEntity entity = new RestaurantEntity();
        entity.setName("OSM Found Restaurant");
        entity.setAddress("OSM Translated Address");
        entity.setLatitude(lat + 0.02);
        entity.setLongitude(lng - 0.02);
        restaurants.add(entity);

        return restaurants;
    }
}