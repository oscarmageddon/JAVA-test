package com.example.userservice.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testUsername = "test@domain.cl";
    private final String testSecret = "testSecretKey";
    private final Long testExpiration = 86400L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);
    }

    @Test
    void generateToken_ValidUsername_ReturnsToken() {

        String token = jwtUtil.generateToken(testUsername);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void extractUsername_ValidToken_ReturnsUsername() {

        String token = jwtUtil.generateToken(testUsername);
        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void extractExpiration_ValidToken_ReturnsExpirationDate() {

        String token = jwtUtil.generateToken(testUsername);
        Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {

        String token = jwtUtil.generateToken(testUsername);
        Boolean isValid = jwtUtil.validateToken(token, testUsername);

        assertTrue(isValid);
    }

    @Test
    void validateToken_WrongUsername_ReturnsFalse() {

        String token = jwtUtil.generateToken(testUsername);
        String wrongUsername = "wrong@domain.cl";
        Boolean isValid = jwtUtil.validateToken(token, wrongUsername);

        assertFalse(isValid);
    }
    
    @Test
    void validateToken_ExpiredToken_ReturnsFalse() {

        ReflectionTestUtils.setField(jwtUtil, "expiration", -1L);
        String expiredToken = jwtUtil.generateToken(testUsername);

        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);

        Boolean isValid = jwtUtil.validateToken(expiredToken, testUsername);

        assertFalse(isValid);
    }
}