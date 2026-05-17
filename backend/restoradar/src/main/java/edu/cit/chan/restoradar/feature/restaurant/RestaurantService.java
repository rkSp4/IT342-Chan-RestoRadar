package edu.cit.chan.restoradar.feature.restaurant;

import edu.cit.chan.restoradar.shared.exception.ForbiddenException;
import edu.cit.chan.restoradar.shared.exception.ResourceNotFoundException;
import edu.cit.chan.restoradar.shared.util.ApiResponse;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // ── GET /restaurants ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<RestaurantDto.Summary> getAllRestaurants(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return restaurantRepository.findAll(pageable).map(this::toSummary);
    }

    // ── GET /restaurants/{id} ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public RestaurantDto.Detail getRestaurantById(UUID id) {
        return toDetail(findById(id));
    }

    // ── GET /restaurants/search ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<RestaurantDto.Summary> search(String query, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return restaurantRepository.searchByNameOrCuisine(query, pageable).map(this::toSummary);
    }

    // ── GET /restaurants/nearby ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<RestaurantDto.Summary> getNearby(double lat, double lng,
                                                  double radius, String priceRange,
                                                  int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Object[]> rows = restaurantRepository.findNearby(lat, lng,
                Math.min(radius, 20), priceRange, pageable);

        java.util.List<RestaurantDto.Summary> summaries = rows.getContent().stream().map(row -> {
            RestaurantEntity r = (RestaurantEntity) row[0];
            double distance = ((Number) row[1]).doubleValue();
            RestaurantDto.Summary s = toSummary(r);
            s.setDistance(Math.round(distance * 10.0) / 10.0);
            return s;
        }).toList();

        return new org.springframework.data.domain.PageImpl<>(summaries, pageable, rows.getTotalElements());
    }

    // ── POST /restaurants ─────────────────────────────────────────────────────

    @Transactional
    public RestaurantDto.Detail createRestaurant(RestaurantDto.Request req) {
        RestaurantEntity r = new RestaurantEntity();
        r.setName(req.getName());
        r.setAddress(req.getAddress());
        r.setLatitude(req.getLatitude());
        r.setLongitude(req.getLongitude());
        r.setWebsite(req.getWebsite());
        r.setContactNumber(req.getContactNumber());
        r.setOperatingHours(req.getOperatingHours());
        r.setCuisineType(req.getCuisineType());
        r.setPriceRange(req.getPriceRange());
        r.setPhotos(req.getPhotos());
        r.setAverageRating(0.0);
        r.setReviewCount(0);
        return toDetail(restaurantRepository.save(r));
    }

    // ── PUT /restaurants/{id} ─────────────────────────────────────────────────

    @Transactional
    public RestaurantDto.Detail updateRestaurant(UUID id, RestaurantDto.Request req, String requesterRole) {
        if (!"ADMIN".equals(requesterRole) && !"OWNER".equals(requesterRole)) {
            throw new ForbiddenException("Only admins or owners can update restaurants");
        }
        RestaurantEntity r = findById(id);
        if (req.getName() != null)           r.setName(req.getName());
        if (req.getAddress() != null)        r.setAddress(req.getAddress());
        if (req.getLatitude() != null)       r.setLatitude(req.getLatitude());
        if (req.getLongitude() != null)      r.setLongitude(req.getLongitude());
        if (req.getWebsite() != null)        r.setWebsite(req.getWebsite());
        if (req.getContactNumber() != null)  r.setContactNumber(req.getContactNumber());
        if (req.getOperatingHours() != null) r.setOperatingHours(req.getOperatingHours());
        if (req.getCuisineType() != null)    r.setCuisineType(req.getCuisineType());
        if (req.getPriceRange() != null)     r.setPriceRange(req.getPriceRange());
        if (req.getPhotos() != null)         r.setPhotos(req.getPhotos());
        return toDetail(restaurantRepository.save(r));
    }

    // ── DELETE /restaurants/{id} ──────────────────────────────────────────────

    @Transactional
    public void deleteRestaurant(UUID id, String requesterRole) {
        if (!"ADMIN".equals(requesterRole)) {
            throw new ForbiddenException("Only admins can delete restaurants");
        }
        restaurantRepository.delete(findById(id));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private RestaurantEntity findById(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    }

    private RestaurantDto.Summary toSummary(RestaurantEntity r) {
        return RestaurantDto.Summary.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .cuisineType(r.getCuisineType())
                .priceRange(r.getPriceRange())
                .averageRating(r.getAverageRating())
                .reviewCount(r.getReviewCount())
                .photos(r.getPhotos())
                .build();
    }

    private RestaurantDto.Detail toDetail(RestaurantEntity r) {
        return RestaurantDto.Detail.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .website(r.getWebsite())
                .contactNumber(r.getContactNumber())
                .operatingHours(r.getOperatingHours())
                .cuisineType(r.getCuisineType())
                .priceRange(r.getPriceRange())
                .averageRating(r.getAverageRating())
                .reviewCount(r.getReviewCount())
                .photos(r.getPhotos())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
