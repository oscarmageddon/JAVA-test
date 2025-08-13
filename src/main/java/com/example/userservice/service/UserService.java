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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse createUser(UserSignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        List<Phone> phones = Optional.ofNullable(request.getPhones())
            .map(phoneList -> phoneList.stream()
                .map(dto -> new Phone(dto.getNumber(), dto.getCitycode(), dto.getContrycode()))
                .collect(Collectors.toList()))
            .orElse(null);

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName(), phones);
        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    public UserResponse loginUser(String token) {
        String email = jwtUtil.extractUsername(token);
        if (!jwtUtil.validateToken(token, email)) {
            throw new UserNotFoundException("Invalid token");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Genera nuevo token y actualiza el ultimo login
        String newToken = jwtUtil.generateToken(user.getEmail());
        user.setToken(newToken);
        user.setLastLogin(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setCreated(user.getCreated());
        response.setLastLogin(user.getLastLogin());
        response.setToken(user.getToken());
        response.setIsActive(user.getIsActive());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());

        if (user.getPhones() != null) {
            List<PhoneDto> phoneDtos = user.getPhones().stream()
                .map(phone -> new PhoneDto(phone.getNumber(), phone.getCitycode(), phone.getContrycode()))
                .collect(Collectors.toList());
            response.setPhones(phoneDtos);
        }

        return response;
    }
}