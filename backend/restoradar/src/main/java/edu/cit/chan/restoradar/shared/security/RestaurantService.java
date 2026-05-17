package edu.cit.chan.restoradar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cit.chan.restoradar.dto.RestaurantDto;
import edu.cit.chan.restoradar.entity.Restaurant;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.exception.ResourceNotFoundException;
import edu.cit.chan.restoradar.exception.ForbiddenException;
import edu.cit.chan.restoradar.repository.RestaurantRepository;
import edu.cit.chan.restoradar.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // ── GET /restaurants ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<RestaurantDto.Summary> getAllRestaurants(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return restaurantRepository.findByApprovedTrue(pageable)
                .map(this::toSummary);
    }

    // ── GET /restaurants/{id} ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public RestaurantDto.Detail getRestaurantById(UUID id) {
        Restaurant r = findApprovedById(id);
        return toDetail(r);
    }

    // ── GET /restaurants/search ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<RestaurantDto.Summary> search(String query, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return restaurantRepository.searchByNameOrCuisine(query, pageable)
                .map(this::toSummary);
    }

    // ── GET /restaurants/nearby ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<RestaurantDto.Summary> getNearby(
            double lat, double lng,
            double radius, String priceRange,
            int page, int limit) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Object[]> rows = restaurantRepository.findNearby(lat, lng, radius, priceRange, pageable);

        List<RestaurantDto.Summary> summaries = rows.getContent().stream().map(row -> {
            // row[0] = Restaurant entity, row[1] = computed distance
            Restaurant r = (Restaurant) row[0];
            double distance = ((Number) row[1]).doubleValue();
            RestaurantDto.Summary s = toSummary(r);
            s.setDistance(Math.round(distance * 10.0) / 10.0);
            return s;
        }).toList();

        return new PageImpl<>(summaries, pageable, rows.getTotalElements());
    }

    // ── POST /restaurants ─────────────────────────────────────────────────────

    @Transactional
    public RestaurantDto.Detail createRestaurant(RestaurantDto.Request req, UUID ownerId) {
        UserEntity owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant r = Restaurant.builder()
                .name(req.getName())
                .description(req.getDescription())
                .priceRange(req.getPriceRange())
                .cuisine(req.getCuisine())
                .address(req.getAddress())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .contactNumber(req.getContactNumber())
                .website(req.getWebsite())
                .hours(req.getHours())
                .photos(req.getPhotos() != null ? req.getPhotos() : new ArrayList<>())
                .owner(owner)
                .approved(false)    // requires admin approval per SDD
                .rating(0.0)
                .reviewCount(0)
                .build();

        return toDetail(restaurantRepository.save(r));
    }

    // ── PUT /restaurants/{id} ─────────────────────────────────────────────────

    @Transactional
    public RestaurantDto.Detail updateRestaurant(UUID id, RestaurantDto.Request req, UUID requesterId, String requesterRole) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        boolean isAdmin = "ADMIN".equals(requesterRole);
        boolean isOwner = r.getOwner() != null && r.getOwner().getId().equals(requesterId);

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You do not have permission to update this restaurant");
        }

        if (req.getName() != null)          r.setName(req.getName());
        if (req.getDescription() != null)   r.setDescription(req.getDescription());
        if (req.getPriceRange() != null)    r.setPriceRange(req.getPriceRange());
        if (req.getCuisine() != null)       r.setCuisine(req.getCuisine());
        if (req.getAddress() != null)       r.setAddress(req.getAddress());
        if (req.getLatitude() != null)      r.setLatitude(req.getLatitude());
        if (req.getLongitude() != null)     r.setLongitude(req.getLongitude());
        if (req.getContactNumber() != null) r.setContactNumber(req.getContactNumber());
        if (req.getWebsite() != null)       r.setWebsite(req.getWebsite());
        if (req.getHours() != null)         r.setHours(req.getHours());
        if (req.getPhotos() != null)        r.setPhotos(req.getPhotos());

        return toDetail(restaurantRepository.save(r));
    }

    // ── DELETE /restaurants/{id} ──────────────────────────────────────────────

    @Transactional
    public void deleteRestaurant(UUID id, String requesterRole) {
        if (!"ADMIN".equals(requesterRole)) {
            throw new ForbiddenException("Only admins can delete restaurants");
        }
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        restaurantRepository.delete(r);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Restaurant findApprovedById(UUID id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        if (!r.getApproved()) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        return r;
    }

    private RestaurantDto.Summary toSummary(Restaurant r) {
        String imageUrl = (r.getPhotos() != null && !r.getPhotos().isEmpty())
                ? r.getPhotos().get(0) : null;

        return RestaurantDto.Summary.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .priceRange(r.getPriceRange())
                .rating(r.getRating())
                .reviewCount(r.getReviewCount())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .imageUrl(imageUrl)
                .build();
    }

    private RestaurantDto.Detail toDetail(Restaurant r) {
        Object parsedHours = null;
        if (r.getHours() != null) {
            try {
                parsedHours = objectMapper.readValue(r.getHours(), new TypeReference<Map<String, String>>() {});
            } catch (Exception ignored) {
                parsedHours = r.getHours();
            }
        }

        return RestaurantDto.Detail.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .priceRange(r.getPriceRange())
                .cuisine(r.getCuisine())
                .address(r.getAddress())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .contactNumber(r.getContactNumber())
                .website(r.getWebsite())
                .hours(parsedHours)
                .rating(r.getRating())
                .reviewCount(r.getReviewCount())
                .photos(r.getPhotos())
                .ownerId(r.getOwner() != null ? r.getOwner().getId() : null)
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
