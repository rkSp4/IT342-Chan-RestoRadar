package edu.cit.chan.restoradar.feature.restaurant;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class RestaurantDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Address is required")
        private String address;

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
        private Double longitude;

        private String website;
        private String contactNumber;
        private String operatingHours;
        private String cuisineType;

        @Pattern(regexp = "^P{1,3}$", message = "Price range must be P, PP, or PPP")
        private String priceRange;

        private java.util.List<String> photos;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Summary {
        private UUID id;
        private String name;
        private String address;
        private String cuisineType;
        private String priceRange;
        private Double averageRating;
        private Integer reviewCount;
        private Double distance;
        private java.util.List<String> photos;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Detail {
        private UUID id;
        private String name;
        private String address;
        private Double latitude;
        private Double longitude;
        private String website;
        private String contactNumber;
        private String operatingHours;
        private String cuisineType;
        private String priceRange;
        private Double averageRating;
        private Integer reviewCount;
        private java.util.List<String> photos;
        private java.time.Instant createdAt;
        private java.time.Instant updatedAt;
    }
}
