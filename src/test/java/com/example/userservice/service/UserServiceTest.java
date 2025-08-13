package com.example.userservice.service;

import com.example.userservice.dto.PhoneDto;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSignUpRequest;
import com.example.userservice.entity.Phone;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private UserSignUpRequest userSignUpRequest;
    private User user;
    private PhoneDto phoneDto;
    private Phone phone;

    @BeforeEach
    void setUp() {
        phoneDto = new PhoneDto(87650009L, 7, "25");
        phone = new Phone(87650009L, 7, "25");
        
        userSignUpRequest = new UserSignUpRequest(
            "test@domain.cl",
            "aB2defgh89",
            "Test User",
            Arrays.asList(phoneDto)
        );

        user = new User(
            "test@domain.cl",
            "encodedPassword",
            "Test User",
            Arrays.asList(phone)
        );
        user.setId(UUID.randomUUID());
        user.setToken("test-token");
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);
    }

    @Test
    void createUser_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("test-token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserResponse result = userService.createUser(userSignUpRequest);

        // Then
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getToken(), result.getToken());
        assertEquals(user.getIsActive(), result.getIsActive());
        assertNotNull(result.getPhones());
        assertEquals(1, result.getPhones().size());
        
        verify(userRepository).existsByEmail(userSignUpRequest.getEmail());
        verify(passwordEncoder).encode(userSignUpRequest.getPassword());
        verify(jwtUtil).generateToken(userSignUpRequest.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_UserAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(userSignUpRequest);
        });

        verify(userRepository).existsByEmail(userSignUpRequest.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtUtil, never()).generateToken(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_WithNullPhones_Success() {
        // Given
        userSignUpRequest.setPhones(null);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("test-token");
        
        User userWithoutPhones = new User(
            "test@domain.cl",
            "encodedPassword",
            "Test User",
            null
        );
        userWithoutPhones.setId(UUID.randomUUID());
        userWithoutPhones.setToken("test-token");
        userWithoutPhones.setCreated(LocalDateTime.now());
        userWithoutPhones.setLastLogin(LocalDateTime.now());
        userWithoutPhones.setIsActive(true);
        
        when(userRepository.save(any(User.class))).thenReturn(userWithoutPhones);

        // When
        UserResponse result = userService.createUser(userSignUpRequest);

        // Then
        assertNotNull(result);
        assertEquals(userWithoutPhones.getEmail(), result.getEmail());
        assertNull(result.getPhones());
        
        verify(userRepository).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        // Given
        String token = "valid-token";
        String email = "test@domain.cl";
        String newToken = "new-token";
        
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(jwtUtil.validateToken(token, email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(email)).thenReturn(newToken);
        
        User updatedUser = user;
        updatedUser.setToken(newToken);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserResponse result = userService.loginUser(token);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(newToken, result.getToken());
        
        verify(jwtUtil).extractUsername(token);
        verify(jwtUtil).validateToken(token, email);
        verify(userRepository).findByEmail(email);
        verify(jwtUtil).generateToken(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void loginUser_InvalidToken_ThrowsException() {
        // Given
        String token = "invalid-token";
        String email = "test@domain.cl";
        
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(jwtUtil.validateToken(token, email)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.loginUser(token);
        });

        verify(jwtUtil).extractUsername(token);
        verify(jwtUtil).validateToken(token, email);
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void loginUser_UserNotFound_ThrowsException() {
        // Given
        String token = "valid-token";
        String email = "test@domain.cl";
        
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(jwtUtil.validateToken(token, email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.loginUser(token);
        });

        verify(jwtUtil).extractUsername(token);
        verify(jwtUtil).validateToken(token, email);
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
}