package com.example.desafiofinal.Auth.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest
{
    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());

        String token = jwtService.getToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }


    @Test
    public void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());

        String token = jwtService.getToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenInvalidWhenUsernameMismatch() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());

        String token = jwtService.getToken(userDetails);

        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("differentUser");

        assertFalse(jwtService.isTokenValid(token, differentUser));
    }

    @Test
    public void testGetUsernameFromToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());

        String token = jwtService.getToken(userDetails);

        String username = jwtService.getUsernameFromToken(token);

        assertEquals("testUser", username);
    }

    @Test
    public void testTokenExpiration() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());

        String token = jwtService.getToken(userDetails);

        assertFalse(jwtService.isTokenExpired(token));
    }
}