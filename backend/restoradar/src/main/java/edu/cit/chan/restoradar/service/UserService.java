package edu.cit.chan.restoradar.service;

import edu.cit.chan.restoradar.dto.UserDto;
import edu.cit.chan.restoradar.entity.UserEntity;
import edu.cit.chan.restoradar.exception.ForbiddenException;
import edu.cit.chan.restoradar.exception.ResourceNotFoundException;
import edu.cit.chan.restoradar.repository.FavoriteRepository;
import edu.cit.chan.restoradar.repository.ReviewRepository;
import edu.cit.chan.restoradar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    // ── GET /users/{id} ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public UserDto.Response getUserProfile(UUID id) {
        UserEntity user = findById(id);
        return toResponse(user);
    }

    // ── PUT /users/{id} ───────────────────────────────────────────────────────

    @Transactional
    public UserDto.Response updateUserProfile(UUID id, UserDto.UpdateRequest req, UUID requesterId) {
        if (!requesterId.equals(id)) {
            throw new ForbiddenException("You can only update your own profile");
        }

        UserEntity user = findById(id);

        if (req.getFullName() != null)    user.setFullName(req.getFullName());
        if (req.getProfileImage() != null) user.setProfileImage(req.getProfileImage());

        // If changing email, make sure it isn't already taken
        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new ForbiddenException("Email is already in use");
            }
            user.setEmail(req.getEmail());
        }

        return toResponse(userRepository.save(user));
    }

    // ── DELETE /users/{id} ────────────────────────────────────────────────────

    @Transactional
    public void deleteUser(UUID id, UUID requesterId, String requesterRole) {
        boolean isAdmin = "ADMIN".equals(requesterRole);
        boolean isSelf  = requesterId.equals(id);

        if (!isAdmin && !isSelf) {
            throw new ForbiddenException("You can only delete your own account");
        }

        UserEntity user = findById(id);
        userRepository.delete(user);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private UserEntity findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserDto.Response toResponse(UserEntity user) {
        int reviewCount   = reviewRepository.countByUserId(user.getId());
        int favoriteCount = favoriteRepository.countByUserId(user.getId());

        return UserDto.Response.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .joinDate(user.getCreatedAt())
                .reviewCount(reviewCount)
                .favoriteCount(favoriteCount)
                .build();
    }
}