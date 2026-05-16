package edu.cit.chan.restoradar.controller;

import edu.cit.chan.restoradar.dto.ReviewDto;
import edu.cit.chan.restoradar.service.ReviewService;
import edu.cit.chan.restoradar.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ── GET /restaurants/{id}/reviews ─────────────────────────────────────────

    @GetMapping("/api/v1/restaurants/{id}/reviews")
    public ResponseEntity<ApiResponse> getReviews(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        Page<ReviewDto.Response> result = reviewService.getReviewsForRestaurant(id, sort, page, limit);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "reviews", result.getContent(),
            "pagination", Map.of(
                "page", result.getNumber() + 1,
                "limit", result.getSize(),
                "total", result.getTotalElements(),
                "pages", result.getTotalPages()
            )
        )));
    }

    // ── POST /restaurants/{id}/reviews ────────────────────────────────────────

    @PostMapping("/api/v1/restaurants/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> addReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        ReviewDto.Response created = reviewService.addReview(id, request, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("review", created)));
    }

    // ── PUT /reviews/{id} ─────────────────────────────────────────────────────

    @PutMapping("/api/v1/reviews/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        ReviewDto.Response updated = reviewService.updateReview(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("review", updated)));
    }

    // ── DELETE /reviews/{id} ──────────────────────────────────────────────────

    @DeleteMapping("/api/v1/reviews/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        String role = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst().orElse("USER");

        reviewService.deleteReview(id, userId, role);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── GET /users/{id}/reviews ───────────────────────────────────────────────

    @GetMapping("/api/v1/users/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getUserReviews(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        Page<ReviewDto.Response> result = reviewService.getReviewsByUser(id, page, limit);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "reviews", result.getContent(),
            "pagination", Map.of(
                "page", result.getNumber() + 1,
                "limit", result.getSize(),
                "total", result.getTotalElements(),
                "pages", result.getTotalPages()
            )
        )));
    }
}