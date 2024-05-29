package com.example.desafiofinal.Auth;

import com.example.desafiofinal.Auth.Jwt.JwtService;
import com.example.desafiofinal.model.Roles;
import com.example.desafiofinal.model.User;
import com.example.desafiofinal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest
{

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        User user = User.builder()
                .username("testUser")
                .password("encodedPassword")
                .rol(Roles.USER)
                .build();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("testToken");

        AuthResponse authResponse = authService.login(loginRequest);

        assertNotNull(authResponse);
        assertEquals("testToken", authResponse.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testLoginFailure() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testRegisterSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("newUser", "newPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("newUserToken");

        AuthResponse authResponse = authService.register(registerRequest);

        assertNotNull(authResponse);
        assertEquals("newUserToken", authResponse.getToken());

        verify(userRepository, times(1)).save(any(User.class));
    }
}