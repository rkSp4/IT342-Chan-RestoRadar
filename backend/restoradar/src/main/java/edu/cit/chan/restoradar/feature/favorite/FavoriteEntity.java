package edu.cit.chan.restoradar.feature.favorite;

import edu.cit.chan.restoradar.feature.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "favorites",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "restaurant_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity restaurant;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}