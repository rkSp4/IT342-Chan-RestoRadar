package edu.cit.chan.restoradar.controller;

import edu.cit.chan.restoradar.dto.AdminDto;
import edu.cit.chan.restoradar.service.AdminService;
import edu.cit.chan.restoradar.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ── GET /admin/users ──────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<AdminDto.UserSummary> result = adminService.getAllUsers(page, limit);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "users", result.getContent(),
            "pagination", Map.of(
                "page", result.getNumber() + 1,
                "limit", result.getSize(),
                "total", result.getTotalElements(),
                "pages", result.getTotalPages()
            )
        )));
    }

    // ── PUT /admin/users/{id}/role ────────────────────────────────────────────

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse> updateUserRole(
            @PathVariable UUID id,
            @Valid @RequestBody AdminDto.RoleUpdateRequest request) {

        AdminDto.UserSummary updated = adminService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok(ApiResponse.success(Map.of("user", updated)));
    }

    // ── DELETE /admin/users/{id} ──────────────────────────────────────────────

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── GET /admin/restaurants/pending ────────────────────────────────────────

    @GetMapping("/restaurants/pending")
    public ResponseEntity<ApiResponse> getPendingRestaurants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Page<AdminDto.PendingRestaurant> result = adminService.getPendingRestaurants(page, limit);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "pendingRestaurants", result.getContent(),
            "pagination", Map.of(
                "page", result.getNumber() + 1,
                "limit", result.getSize(),
                "total", result.getTotalElements(),
                "pages", result.getTotalPages()
            )
        )));
    }

    // ── PUT /admin/restaurants/{id}/approve ───────────────────────────────────

    @PutMapping("/restaurants/{id}/approve")
    public ResponseEntity<ApiResponse> approveRestaurant(@PathVariable UUID id) {
        AdminDto.PendingRestaurant approved = adminService.approveRestaurant(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("restaurant", approved)));
    }

    // ── DELETE /admin/reviews/{id} ────────────────────────────────────────────

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable UUID id) {
        adminService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}