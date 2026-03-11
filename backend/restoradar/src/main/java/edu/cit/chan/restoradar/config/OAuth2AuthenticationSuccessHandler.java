package edu.cit.chan.restoradar.config;

import edu.cit.chan.restoradar.entity.RefreshTokenEntity;
import edu.cit.chan.restoradar.entity.UserEntity;
import edu.cit.chan.restoradar.repository.UserRepository;
import edu.cit.chan.restoradar.service.AuthService;
import edu.cit.chan.restoradar.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthService authService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public OAuth2AuthenticationSuccessHandler(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthService authService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        if (email == null || email.isBlank()) {
            response.sendRedirect(frontendUrl + "/login?error=google_email_missing");
            return;
        }

        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setFullName((name == null || name.isBlank()) ? "Google User" : name);
            newUser.setProfileImage(picture);
            newUser.setRole("USER");
            newUser.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(newUser);
        });

        String accessToken = jwtService.generateToken(user);
        RefreshTokenEntity refreshToken = authService.createRefreshToken(user);

        String redirectUrl = frontendUrl + "/oauth/callback"
                + "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken.getToken(), StandardCharsets.UTF_8)
                + "&id=" + URLEncoder.encode(user.getId().toString(), StandardCharsets.UTF_8)
                + "&fullName=" + URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(user.getRole(), StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}