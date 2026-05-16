package edu.cit.chan.restoradar.controller;

import edu.cit.chan.restoradar.dto.RestaurantDto;
import edu.cit.chan.restoradar.service.RestaurantService;
import edu.cit.chan.restoradar.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // ── GET /restaurants ──────────────────────────────────────────────────────
    // Returns paginated list of all approved restaurants.

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRestaurants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<RestaurantDto.Summary> result = restaurantService.getAllRestaurants(page, limit);
        return ResponseEntity.ok(ApiResponse.success(buildPaginatedBody("restaurants", result)));
    }

    // ── GET /restaurants/search ───────────────────────────────────────────────
    // Declared before /{id} to avoid Spring mapping /search as a UUID.

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<RestaurantDto.Summary> result = restaurantService.search(query, page, limit);
        return ResponseEntity.ok(ApiResponse.success(buildPaginatedBody("restaurants", result)));
    }

    // ── GET /restaurants/nearby ───────────────────────────────────────────────

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse> nearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radius,
            @RequestParam(required = false) String priceRange,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        double clampedRadius = Math.min(radius, 20);
        Page<RestaurantDto.Summary> result =
                restaurantService.getNearby(lat, lng, clampedRadius, priceRange, page, limit);
        return ResponseEntity.ok(ApiResponse.success(buildPaginatedBody("restaurants", result)));
    }

    // ── GET /restaurants/{id} ─────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        RestaurantDto.Detail detail = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(ApiResponse.success(java.util.Map.of("restaurant", detail)));
    }

    // ── POST /restaurants ─────────────────────────────────────────────────────
    // Requires authentication. Role USER creates a pending listing;
    // ADMIN creates an auto-approved listing.

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody RestaurantDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID ownerId = extractUserId(userDetails);
        RestaurantDto.Detail created = restaurantService.createRestaurant(request, ownerId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(java.util.Map.of("restaurant", created)));
    }

    // ── PUT /restaurants/{id} ─────────────────────────────────────────────────
    // Owner of the restaurant or ADMIN can update.

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID requesterId = extractUserId(userDetails);
        String role = extractRole(userDetails);
        RestaurantDto.Detail updated = restaurantService.updateRestaurant(id, request, requesterId, role);
        return ResponseEntity.ok(ApiResponse.success(java.util.Map.of("restaurant", updated)));
    }

    // ── DELETE /restaurants/{id} ──────────────────────────────────────────────
    // Admin only.

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        restaurantService.deleteRestaurant(id, extractRole(userDetails));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private java.util.Map<String, Object> buildPaginatedBody(String key, Page<?> page) {
        return java.util.Map.of(
            key, page.getContent(),
            "pagination", java.util.Map.of(
                "page", page.getNumber() + 1,
                "limit", page.getSize(),
                "total", page.getTotalElements(),
                "pages", page.getTotalPages()
            )
        );
    }

    private UUID extractUserId(UserDetails userDetails) {
        // Your UserDetailsServiceImpl sets .username(userEntity.getId().toString())
        // so getUsername() returns the UUID string directly.
        return UUID.fromString(userDetails.getUsername());
    }

    private String extractRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("USER");
    }
}