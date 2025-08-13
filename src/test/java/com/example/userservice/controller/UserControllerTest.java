package com.example.userservice.controller;

import com.example.userservice.dto.PhoneDto;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSignUpRequest;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserSignUpRequest userSignUpRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        PhoneDto phoneDto = new PhoneDto(87650009L, 7, "25");
        
        userSignUpRequest = new UserSignUpRequest(
            "test@domain.cl",
            "aB2defgh89",
            "Test User",
            Arrays.asList(phoneDto)
        );

        userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID());
        userResponse.setCreated(LocalDateTime.now());
        userResponse.setLastLogin(LocalDateTime.now());
        userResponse.setToken("test-token");
        userResponse.setIsActive(true);
        userResponse.setName("Test User");
        userResponse.setEmail("test@domain.cl");
        userResponse.setPassword("encodedPassword");
        userResponse.setPhones(Arrays.asList(phoneDto));
    }

    @Test
    void signUp_ValidRequest_ReturnsCreated() throws Exception {

        when(userService.createUser(any(UserSignUpRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@domain.cl"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.isActive").value(true));
    }
    
    @Test
    void signUp_UserAlreadyExists_ReturnsConflict() throws Exception {

        when(userService.createUser(any(UserSignUpRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").isArray())
                .andExpect(jsonPath("$.error[0].detail").value("User already exists"));
    }
    
    @Test
    void signUp_InvalidEmail_ReturnsBadRequest() throws Exception {

        userSignUpRequest.setEmail("invalid-email");

        mockMvc.perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").isArray());
    }

    @Test
    void login_ValidToken_ReturnsUser() throws Exception {
        
        when(userService.loginUser(anyString())).thenReturn(userResponse);

        mockMvc.perform(get("/api/login")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@domain.cl"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void login_InvalidToken_ReturnsNotFound() throws Exception {
        
        when(userService.loginUser(anyString()))
                .thenThrow(new UserNotFoundException("Invalid token"));

        mockMvc.perform(get("/api/login")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").isArray())
                .andExpect(jsonPath("$.error[0].detail").value("Invalid token"));
    }
}