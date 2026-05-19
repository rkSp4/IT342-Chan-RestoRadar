package edu.cit.chan.restoradar.feature.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class FavoriteDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Restaurant ID is required")
        private UUID restaurantId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private UUID id;
        private UUID userId;
        private UUID restaurantId;
        private String restaurantName;
        private String restaurantPriceRange;
        private Double restaurantRating;
        private String restaurantImageUrl;
        private java.time.Instant createdAt;
    }
}
