package edu.cit.chan.restoradar.shared.config;

import edu.cit.chan.restoradar.feature.auth.AuthService;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.feature.user.UserRepository;
import edu.cit.chan.restoradar.shared.entity.RefreshTokenEntity;
import edu.cit.chan.restoradar.shared.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import edu.cit.chan.restoradar.feature.user.CustomerUser;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtService jwtService;

    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository, 
                                              @org.springframework.context.annotation.Lazy AuthService authService, 
                                              JwtService jwtService) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Find or create user
        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            CustomerUser newUser = new CustomerUser();
            newUser.setEmail(email);
            newUser.setFullName(name != null ? name : email);
            newUser.setPasswordHash(""); // OAuth users have no password
            newUser.setRole("USER");
            return userRepository.save(newUser);
        });

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        RefreshTokenEntity refreshToken = authService.createRefreshToken(user);

        // Redirect to frontend callback with tokens as query params
        String redirectUrl = frontendUrl + "/auth/callback" +
                "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8) +
                "&refreshToken=" + URLEncoder.encode(refreshToken.getToken(), StandardCharsets.UTF_8) +
                "&id=" + user.getId().toString() +
                "&fullName=" + URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8) +
                "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8) +
                "&role=" + URLEncoder.encode(user.getRole(), StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}