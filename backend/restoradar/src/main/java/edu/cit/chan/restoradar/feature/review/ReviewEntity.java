package edu.cit.chan.restoradar.feature.review;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(nullable = false)
    private int score; // e.g. 1 to 5

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    public ReviewEntity() {}

    public ReviewEntity(RestaurantEntity restaurant, int score, String comment) {
        this.restaurant = restaurant;
        this.score = score;
        this.comment = comment;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
