package edu.cit.chan.restoradar.feature.restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import edu.cit.chan.restoradar.shared.util.ApiResponse;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // ── GET /restaurants ──────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<RestaurantDto.Summary> result = restaurantService.getAllRestaurants(page, limit);
        return ResponseEntity.ok(ApiResponse.success(paginated("restaurants", result)));
    }

    // ── GET /restaurants/search ───────────────────────────────────────────────

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<RestaurantDto.Summary> result = restaurantService.search(query, page, limit);
        return ResponseEntity.ok(ApiResponse.success(paginated("restaurants", result)));
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

        Page<RestaurantDto.Summary> result =
                restaurantService.getNearby(lat, lng, radius, priceRange, page, limit);
        return ResponseEntity.ok(ApiResponse.success(paginated("restaurants", result)));
    }

    // ── GET /restaurants/{id} ─────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(
                Map.of("restaurant", restaurantService.getRestaurantById(id))));
    }

    // ── POST /restaurants ─────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody RestaurantDto.Request request) {

        RestaurantDto.Detail created = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("restaurant", created)));
    }

    // ── PUT /restaurants/{id} ─────────────────────────────────────────────────

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String role = extractRole(userDetails);
        RestaurantDto.Detail updated = restaurantService.updateRestaurant(id, request, role);
        return ResponseEntity.ok(ApiResponse.success(Map.of("restaurant", updated)));
    }

    // ── DELETE /restaurants/{id} ──────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        restaurantService.deleteRestaurant(id, extractRole(userDetails));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, Object> paginated(String key, Page<?> page) {
        return Map.of(
                key, page.getContent(),
                "pagination", Map.of(
                        "page", page.getNumber() + 1,
                        "limit", page.getSize(),
                        "total", page.getTotalElements(),
                        "pages", page.getTotalPages()
                )
        );
    }

    private String extractRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst().orElse("USER");
    }
}
