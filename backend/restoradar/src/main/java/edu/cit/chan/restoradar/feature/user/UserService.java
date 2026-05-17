package edu.cit.chan.restoradar.feature.user;

import edu.cit.chan.restoradar.feature.favorite.FavoriteRepository;
import edu.cit.chan.restoradar.feature.review.ReviewRepository;
import edu.cit.chan.restoradar.shared.exception.ForbiddenException;
import edu.cit.chan.restoradar.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public UserService(UserRepository userRepository,
                       ReviewRepository reviewRepository,
                       FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserProfile(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public UserDto.Response updateUserProfile(UUID id, UserDto.UpdateRequest req, UUID requesterId) {
        if (!requesterId.equals(id)) {
            throw new ForbiddenException("You can only update your own profile");
        }
        UserEntity user = findById(id);
        if (req.getFullName() != null)    user.setFullName(req.getFullName());
        if (req.getProfileImage() != null) user.setProfileImage(req.getProfileImage());
        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new ForbiddenException("Email is already in use");
            }
            user.setEmail(req.getEmail());
        }
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UUID id, UUID requesterId, String requesterRole) {
        if (!"ADMIN".equals(requesterRole) && !requesterId.equals(id)) {
            throw new ForbiddenException("You can only delete your own account");
        }
        userRepository.delete(findById(id));
    }

    private UserEntity findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserDto.Response toResponse(UserEntity user) {
        return UserDto.Response.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .joinDate(user.getCreatedAt())
                .reviewCount(reviewRepository.countByUserId(user.getId()))
                .favoriteCount(favoriteRepository.countByUserId(user.getId()))
                .build();
    }
}
