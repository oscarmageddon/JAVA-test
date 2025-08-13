package com.example.userservice.controller;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSignUpRequest;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserResponse response = userService.loginUser(token);
        return ResponseEntity.ok(response);
    }
}