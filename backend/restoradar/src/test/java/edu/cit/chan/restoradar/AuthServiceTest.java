package edu.cit.chan.restoradar;

import edu.cit.chan.restoradar.feature.auth.AuthService;
import edu.cit.chan.restoradar.feature.auth.RegistrationRequest;
import edu.cit.chan.restoradar.feature.auth.LoginRequest;
import edu.cit.chan.restoradar.feature.user.UserEntity;
import edu.cit.chan.restoradar.feature.user.CustomerUser;
import edu.cit.chan.restoradar.feature.user.UserRepository;
import edu.cit.chan.restoradar.shared.security.JwtService;
import edu.cit.chan.restoradar.feature.user.UserFactory;
import edu.cit.chan.restoradar.shared.entity.RefreshTokenRepository;
import edu.cit.chan.restoradar.shared.exception.InvalidCredentialsException;
import edu.cit.chan.restoradar.shared.exception.DuplicateEmailException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserFactory userFactory;
    @Mock private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("SecurePass123");
        registrationRequest.setConfirmPassword("SecurePass123");
        registrationRequest.setFullName("Test User");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("SecurePass123");
    }

    @Test
    void register_WithValidData_ShouldReturnUserEntity() {
        CustomerUser mockUser = new CustomerUser();
        mockUser.setEmail("test@example.com");
        mockUser.setFullName("Test User");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userFactory.createUser(any())).thenReturn(mockUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(mockUser);

        UserEntity result = authService.register(registrationRequest);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void register_WithExistingEmail_ShouldThrowDuplicateEmailException() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class,
            () -> authService.register(registrationRequest));
    }

    @Test
    void register_WithPasswordMismatch_ShouldThrowException() {
        registrationRequest.setConfirmPassword("WrongPassword");

        assertThrows(IllegalArgumentException.class,
            () -> authService.register(registrationRequest));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnUserEntity() {
        CustomerUser mockUser = new CustomerUser();
        mockUser.setEmail("test@example.com");
        mockUser.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail("test@example.com"))
            .thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        UserEntity result = authService.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void authenticate_WithWrongPassword_ShouldThrowInvalidCredentialsException() {
        CustomerUser mockUser = new CustomerUser();
        mockUser.setEmail("test@example.com");
        mockUser.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail("test@example.com"))
            .thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
            () -> authService.authenticate(loginRequest));
    }

    @Test
    void authenticate_WithNonExistentEmail_ShouldThrowInvalidCredentialsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
            () -> authService.authenticate(loginRequest));
    }
}