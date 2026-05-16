package edu.cit.chan.restoradar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Instant;
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
    public static class PendingRestaurant {
        private UUID id;
        private String name;
        private UUID ownerId;
        private String ownerName;
        private Instant submittedAt;
        private String status;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class RoleUpdateRequest {

        @NotBlank(message = "Role is required")
        @Pattern(regexp = "^(USER|OWNER|ADMIN)$", message = "Role must be USER, OWNER, or ADMIN")
        private String role;
    }
}