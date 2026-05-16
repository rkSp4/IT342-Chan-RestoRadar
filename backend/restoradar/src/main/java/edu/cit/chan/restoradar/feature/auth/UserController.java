package edu.cit.chan.restoradar.controller;

import edu.cit.chan.restoradar.dto.UserDto;
import edu.cit.chan.restoradar.service.UserService;
import edu.cit.chan.restoradar.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── GET /users/{id} ───────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable UUID id) {
        UserDto.Response user = userService.getUserProfile(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("user", user)));
    }

    // ── PUT /users/{id} ───────────────────────────────────────────────────────

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UserDto.UpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID requesterId = UUID.fromString(userDetails.getUsername());
        UserDto.Response updated = userService.updateUserProfile(id, request, requesterId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("user", updated)));
    }

    // ── DELETE /users/{id} ────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID requesterId = UUID.fromString(userDetails.getUsername());
        String role = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst().orElse("USER");

        userService.deleteUser(id, requesterId, role);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}