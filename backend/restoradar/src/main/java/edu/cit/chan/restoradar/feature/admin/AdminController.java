package edu.cit.chan.restoradar.feature.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.cit.chan.restoradar.shared.util.ApiResponse;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ── Users ─────────────────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<AdminDto.UserSummary> result = adminService.getAllUsers(page, limit);
        return ResponseEntity.ok(ApiResponse.success(paginated("users", result)));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse> updateUserRole(
            @PathVariable UUID id,
            @Valid @RequestBody AdminDto.RoleUpdateRequest request) {

        return ResponseEntity.ok(ApiResponse.success(
                Map.of("user", adminService.updateUserRole(id, request.getRole()))));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── Restaurants ───────────────────────────────────────────────────────────

    @GetMapping("/restaurants")
    public ResponseEntity<ApiResponse> getAllRestaurants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<AdminDto.RestaurantSummary> result = adminService.getAllRestaurants(page, limit);
        return ResponseEntity.ok(ApiResponse.success(paginated("restaurants", result)));
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable UUID id) {
        adminService.deleteRestaurant(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── Reviews ───────────────────────────────────────────────────────────────

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable UUID id) {
        adminService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── Helper ────────────────────────────────────────────────────────────────

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
}
