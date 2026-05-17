// ── FavoriteEntity.java ───────────────────────────────────────────────────────
package edu.cit.chan.restoradar.feature.favorite;

import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "favorites",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "restaurant_id"}))
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public FavoriteEntity() {}

    public FavoriteEntity(UserEntity user, RestaurantEntity restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }

    public UUID getId() { return id; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
