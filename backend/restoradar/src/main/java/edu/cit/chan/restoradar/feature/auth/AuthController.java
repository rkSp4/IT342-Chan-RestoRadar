package edu.cit.chan.restoradar.feature.auth;

import edu.cit.chan.restoradar.shared.dto.*;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.shared.dto.ErrorResponse;
import edu.cit.chan.restoradar.shared.entity.RefreshTokenEntity;
import edu.cit.chan.restoradar.shared.exception.DuplicateEmailException;
import edu.cit.chan.restoradar.shared.exception.InvalidCredentialsException;
import edu.cit.chan.restoradar.shared.exception.TokenRefreshException;
import edu.cit.chan.restoradar.shared.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            UserEntity user = authService.register(request);
            String accessToken = jwtService.generateToken(user);
            RefreshTokenEntity refreshToken = authService.createRefreshToken(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(user, accessToken, refreshToken.getToken()));
        } catch (DuplicateEmailException e) {
            return buildErrorResponse(HttpStatus.CONFLICT, "DB-002", e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "VALID-001", e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserEntity user = authService.authenticate(request);
            String accessToken = jwtService.generateToken(user);
            RefreshTokenEntity refreshToken = authService.createRefreshToken(user);

            return ResponseEntity.ok(new AuthResponse(user, accessToken, refreshToken.getToken()));
        } catch (InvalidCredentialsException e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "AUTH-001", e.getMessage(), null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
        try {
            RefreshTokenEntity token = authService.verifyRefreshToken(refreshToken);
            UserEntity user = token.getUser();
            String newAccessToken = jwtService.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(user, newAccessToken, refreshToken));
        } catch (TokenRefreshException e) {
            return buildErrorResponse(HttpStatus.FORBIDDEN, "AUTH-004", e.getMessage(), null);
        }
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String code, String message, Object details) {
        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail(code, message, details);
        ErrorResponse response = new ErrorResponse(false, null, errorDetail, Instant.now());
        return ResponseEntity.status(status).body(response);
    }
}