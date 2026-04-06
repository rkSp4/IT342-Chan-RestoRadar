package edu.cit.chan.restoradar.facade;

import edu.cit.chan.restoradar.dto.SearchRequest;
import edu.cit.chan.restoradar.entity.RestaurantEntity;
import edu.cit.chan.restoradar.repository.RestaurantRepository;
import edu.cit.chan.restoradar.service.MapService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade Pattern implementation to abstract the complex searching logic.
 * By hiding the internal complexities of parsing, combining geographical searches, 
 * database filtering, calculating reviews/ratings, and pagination, 
 * controllers remain clean and decoupled from the actual logic execution.
 */
@Service
public class RestaurantSearchFacade {

    private final MapService mapService;
    private final RestaurantRepository restaurantRepository;

    public RestaurantSearchFacade(MapService mapService, RestaurantRepository restaurantRepository) {
        this.mapService = mapService;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Executes a fully coordinated search across multiple subsystems.
     * @param request Search Request DTO with user constraints 
     * @return Filtered, scored, and paginated list of RestaurantEntity
     */
    public List<RestaurantEntity> search(SearchRequest request) {
        
        // 1. Parameter Validation (Simplified)
        if (request.getRadiusInKm() <= 0) {
            request.setRadiusInKm(5.0); // Default to 5km radius
        }

        // 2. Call external mapping service to compute/resolve radius bounding
        // Translates real globe coordinates into nearby restaurant potentials
        List<RestaurantEntity> nearbyFromMap = mapService.getNearbyPlaces(
                request.getLat(), 
                request.getLng(), 
                request.getRadiusInKm()
        );

        // 3. Database spatial or textual search simulation
        // Real-world: Could be retrieving all restaurants and applying filters
        List<RestaurantEntity> dbEntities;
        if (request.getPriceRange() != null && !request.getPriceRange().isBlank()) {
            dbEntities = restaurantRepository.findByPriceRange(request.getPriceRange());
        } else {
            dbEntities = restaurantRepository.findAll();
        }

        // 4. Combine subsystems, apply price-range processing / compute ratings
        // A naive aggregation logic for the demo: merge and filter out things out of bounds
        List<RestaurantEntity> results = dbEntities.stream()
                .filter(entity -> calculateDistance(request.getLat(), request.getLng(), entity.getLatitude(), entity.getLongitude()) <= request.getRadiusInKm())
                .collect(Collectors.toList());
        
        results.addAll(nearbyFromMap);

        // 5. Calculate average ratings + Pagination (Mocked here by simply returning a slice)
        // E.g., assign computed scores: result.setAverageRating( ratingCalculator.getAverageFor(result) )

        return applyPagination(results, request.getPage(), request.getSize());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Dummy haversine logic or flat-distance computation to mimic spatial query
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2)) * 111.0; 
    }

    private List<RestaurantEntity> applyPagination(List<RestaurantEntity> list, int page, int size) {
        if (size <= 0) size = 10;
        int skip = page * size;
        return list.stream().skip(skip).limit(size).collect(Collectors.toList());
    }
}
