package edu.cit.chan.restoradar.service;

import edu.cit.chan.restoradar.dto.LoginRequest;
import edu.cit.chan.restoradar.dto.RegistrationRequest;
import edu.cit.chan.restoradar.entity.RefreshTokenEntity;
import edu.cit.chan.restoradar.entity.Role;
import edu.cit.chan.restoradar.entity.UserEntity;
import edu.cit.chan.restoradar.exception.DuplicateEmailException;
import edu.cit.chan.restoradar.exception.InvalidCredentialsException;
import edu.cit.chan.restoradar.exception.TokenRefreshException;
import edu.cit.chan.restoradar.factory.UserFactory;
import edu.cit.chan.restoradar.repository.RefreshTokenRepository;
import edu.cit.chan.restoradar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserFactory userFactory;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration; // in milliseconds

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UserFactory userFactory) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userFactory = userFactory;
    }

    @Transactional
    public UserEntity register(RegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }

        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Determine user role from request or default to USER
        Role role = Role.USER;
        if (request.getRole() != null) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore and use default USER
            }
        }

        // Create user using Factory Method
        UserEntity user = userFactory.createUser(role);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public UserEntity authenticate(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }

    @Transactional
public RefreshTokenEntity createRefreshToken(UserEntity user) {
    // Delete old refresh token if exists
    refreshTokenRepository.deleteByUser(user);
    // Force flush to ensure deletion is committed before insert
    refreshTokenRepository.flush();

    RefreshTokenEntity refreshToken = new RefreshTokenEntity();
    refreshToken.setUser(user);
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));

    return refreshTokenRepository.save(refreshToken);
}

    @Transactional
    public RefreshTokenEntity verifyRefreshToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Invalid refresh token"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException("Refresh token expired");
        }

        return refreshToken;
    }
}