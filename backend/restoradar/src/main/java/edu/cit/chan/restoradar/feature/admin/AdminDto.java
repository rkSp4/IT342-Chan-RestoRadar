// ── AdminDto.java ─────────────────────────────────────────────────────────────
package edu.cit.chan.restoradar.feature.admin;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UserSummary {
        private UUID id;
        private String email;
        private String fullName;
        private String role;
        private LocalDateTime joinDate;
        private int reviewCount;
        private int favoriteCount;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RestaurantSummary {
        private UUID id;
        private String name;
        private String address;
        private Double averageRating;
        private Integer reviewCount;
        private LocalDateTime createdAt;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class RoleUpdateRequest {
        @NotBlank(message = "Role is required")
        @Pattern(regexp = "^(USER|OWNER|ADMIN)$", message = "Role must be USER, OWNER, or ADMIN")
        private String role;
    }
}
