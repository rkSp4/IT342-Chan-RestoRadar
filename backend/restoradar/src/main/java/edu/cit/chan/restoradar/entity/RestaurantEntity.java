package edu.cit.chan.restoradar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
// @Data, @Builder temporarily omitted pending fully resolving Lombok environment setup
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Required fields
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    // Optional fields
    private String website;
    private String contactNumber;
    private String operatingHours;
    private String cuisineType;
    private String priceRange;
    private String photos;
    
    @Column(nullable = false)
    private Double averageRating = 0.0;
    
    @Column(nullable = false)
    private Integer reviewCount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getAverageRating() { return averageRating; }
    public Integer getReviewCount() { return reviewCount; }
    public String getName() { return name; }

    // Default constructor for framework requirement
    public RestaurantEntity() {}
}
