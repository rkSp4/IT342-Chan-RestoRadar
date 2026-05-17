package edu.cit.chan.restoradar.feature.favorite;

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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // ── GET /users/{id}/favorites ─────────────────────────────────────────────

    @GetMapping("/users/{id}/favorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getUserFavorites(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID requesterId = UUID.fromString(userDetails.getUsername());
        String role = extractRole(userDetails);

        if (!requesterId.equals(id) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("AUTH-003", "You can only view your own favorites", null));
        }

        Page<FavoriteDto.Response> result = favoriteService.getUserFavorites(id, page, limit);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "favorites", result.getContent(),
                "pagination", Map.of(
                        "page", result.getNumber() + 1,
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "pages", result.getTotalPages()
                )
        )));
    }

    // ── POST /favorites ───────────────────────────────────────────────────────

    @PostMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> addFavorite(
            @Valid @RequestBody FavoriteDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        FavoriteDto.Response created = favoriteService.addFavorite(request.getRestaurantId(), userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("favorite", created)));
    }

    // ── DELETE /favorites/{restaurantId} ──────────────────────────────────────

    @DeleteMapping("/favorites/{restaurantId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> removeFavorite(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        favoriteService.removeFavorite(restaurantId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private String extractRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst().orElse("USER");
    }
}
