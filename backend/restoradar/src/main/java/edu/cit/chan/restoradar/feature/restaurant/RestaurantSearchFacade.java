package edu.cit.chan.restoradar.feature.restaurant;

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
    private final SortStrategyFactory sortStrategyFactory;

    public RestaurantSearchFacade(MapService mapService, 
                                  RestaurantRepository restaurantRepository,
                                  SortStrategyFactory sortStrategyFactory) {
        this.mapService = mapService;
        this.restaurantRepository = restaurantRepository;
        this.sortStrategyFactory = sortStrategyFactory;
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
        // Calculate distance and filter out out-of-bounds entities
        List<RestaurantEntity> results = dbEntities.stream()
                .peek(entity -> {
                    double distance = calculateDistance(request.getLat(), request.getLng(), entity.getLatitude(), entity.getLongitude());
                    entity.setDistance(distance);
                })
                .filter(entity -> entity.getDistance() <= request.getRadiusInKm())
                .collect(Collectors.toList());
        
        // Similarly prep Map entities (assuming distance is already valid)
        nearbyFromMap.forEach(entity -> {
            double distance = calculateDistance(request.getLat(), request.getLng(), entity.getLatitude(), entity.getLongitude());
            entity.setDistance(distance);
        });
        results.addAll(nearbyFromMap);

        // 5. Setup Sort Strategy automatically resolving 'top-rated', 'nearest', 'most-reviewed'
        SortStrategy sortStrategy = sortStrategyFactory.getStrategy(request.getSortBy());
        sortStrategy.sort(results);

        // 6. Calculate average ratings + Pagination (Mocked here by simply returning a slice)
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
