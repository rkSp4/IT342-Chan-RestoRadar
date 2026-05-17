package edu.cit.chan.restoradar.feature.favorite;

import edu.cit.chan.restoradar.feature.restaurant.RestaurantEntity;
import edu.cit.chan.restoradar.feature.restaurant.RestaurantRepository;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.feature.user.UserRepository;
import edu.cit.chan.restoradar.shared.exception.ForbiddenException;
import edu.cit.chan.restoradar.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           RestaurantRepository restaurantRepository,
                           UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    // ── GET /users/{id}/favorites ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<FavoriteDto.Response> getUserFavorites(UUID userId, int page, int limit) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return favoriteRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    // ── POST /favorites ───────────────────────────────────────────────────────

    @Transactional
    public FavoriteDto.Response addFavorite(UUID restaurantId, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (favoriteRepository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new ForbiddenException("Restaurant is already in your favorites");
        }

        return toResponse(favoriteRepository.save(new FavoriteEntity(user, restaurant)));
    }

    // ── DELETE /favorites/{restaurantId} ──────────────────────────────────────

    @Transactional
    public void removeFavorite(UUID restaurantId, UUID userId) {
        FavoriteEntity favorite = favoriteRepository
                .findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private FavoriteDto.Response toResponse(FavoriteEntity f) {
        RestaurantEntity r = f.getRestaurant();
        return FavoriteDto.Response.builder()
                .id(f.getId())
                .userId(f.getUser().getId())
                .restaurantId(r.getId())
                .restaurantName(r.getName())
                .restaurantPriceRange(r.getPriceRange())
                .restaurantRating(r.getAverageRating())
                .createdAt(f.getCreatedAt())
                .build();
    }
}
