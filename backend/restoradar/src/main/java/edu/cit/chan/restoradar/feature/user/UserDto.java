// ── UserDto.java ──────────────────────────────────────────────────────────────
package edu.cit.chan.restoradar.feature.user;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private UUID id;
        private String email;
        private String fullName;
        private String profileImage;
        private String role;
        private LocalDateTime joinDate;
        private int reviewCount;
        private int favoriteCount;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class UpdateRequest {
        @Email(message = "Invalid email format")
        private String email;
        private String fullName;
        private String profileImage;
    }
}
