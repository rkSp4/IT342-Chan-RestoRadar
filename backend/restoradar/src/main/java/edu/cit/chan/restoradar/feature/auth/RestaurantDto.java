package edu.cit.chan.restoradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RestaurantDto {

    // ── Request: create / update ──────────────────────────────────────────────

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotBlank(message = "Price range is required")
        @Pattern(regexp = "^P{1,3}$", message = "Price range must be P, PP, or PPP")
        private String priceRange;

        private String cuisine;

        @NotBlank(message = "Address is required")
        private String address;

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
        private Double longitude;

        private String contactNumber;
        private String website;
        private String hours;           // JSON string
        private List<String> photos;
    }

    // ── Response: list / detail ───────────────────────────────────────────────

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Summary {
        private UUID id;
        private String name;
        private String description;
        private String priceRange;
        private Double rating;
        private Integer reviewCount;
        private Double distance;        // only present in /nearby responses
        private Double latitude;
        private Double longitude;
        private String imageUrl;        // first photo, if any
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Detail {
        private UUID id;
        private String name;
        private String description;
        private String priceRange;
        private String cuisine;
        private String address;
        private Double latitude;
        private Double longitude;
        private String contactNumber;
        private String website;
        private Object hours;           // parsed JSON object
        private Double rating;
        private Integer reviewCount;
        private List<String> photos;
        private UUID ownerId;
        private Instant createdAt;
        private Instant updatedAt;
    }
}