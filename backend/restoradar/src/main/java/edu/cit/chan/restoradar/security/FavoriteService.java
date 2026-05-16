package edu.cit.chan.restoradar.service;

import edu.cit.chan.restoradar.dto.FavoriteDto;
import edu.cit.chan.restoradar.entity.Favorite;
import edu.cit.chan.restoradar.entity.Restaurant;
import edu.cit.chan.restoradar.entity.UserEntity;
import edu.cit.chan.restoradar.exception.ForbiddenException;
import edu.cit.chan.restoradar.exception.ResourceNotFoundException;
import edu.cit.chan.restoradar.repository.FavoriteRepository;
import edu.cit.chan.restoradar.repository.RestaurantRepository;
import edu.cit.chan.restoradar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

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
    public FavoriteDto.Response addFavorite(FavoriteDto.Request req, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (favoriteRepository.existsByUserIdAndRestaurantId(userId, req.getRestaurantId())) {
            throw new ForbiddenException("Restaurant is already in your favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRestaurant(restaurant);

        return toResponse(favoriteRepository.save(favorite));
    }

    // ── DELETE /favorites/{restaurantId} ──────────────────────────────────────

    @Transactional
    public void removeFavorite(UUID restaurantId, UUID userId) {
        Favorite favorite = favoriteRepository
                .findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        if (!favorite.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only remove your own favorites");
        }

        favoriteRepository.delete(favorite);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private FavoriteDto.Response toResponse(Favorite f) {
        Restaurant r = f.getRestaurant();
        String imageUrl = (r.getPhotos() != null && !r.getPhotos().isEmpty())
                ? r.getPhotos().get(0) : null;

        return FavoriteDto.Response.builder()
                .id(f.getId())
                .userId(f.getUser().getId())
                .restaurantId(r.getId())
                .restaurantName(r.getName())
                .restaurantPriceRange(r.getPriceRange())
                .restaurantRating(r.getRating())
                .restaurantImageUrl(imageUrl)
                .createdAt(f.getCreatedAt())
                .build();
    }
}