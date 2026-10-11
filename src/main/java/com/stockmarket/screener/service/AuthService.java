package com.stockmarket.screener.service;

import com.stockmarket.screener.dto.AuthResponse;
import com.stockmarket.screener.dto.LoginRequest;
import com.stockmarket.screener.dto.RegisterRequest;
import com.stockmarket.screener.dto.UpdateProfileRequest;
import com.stockmarket.screener.dto.UserResponse;
import com.stockmarket.screener.entity.User;
import com.stockmarket.screener.repository.UserRepository;
import com.stockmarket.screener.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getName().trim(), request.getEmail().trim().toLowerCase(), encodedPassword, "ROLE_USER");
        user = userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());
        UserResponse userResponse = new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());

        return new AuthResponse(token, userResponse);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        UserResponse userResponse = new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());

        return new AuthResponse(token, userResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }

    @Transactional
    public AuthResponse updateProfile(String currentEmail, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newEmail = request.getEmail().trim().toLowerCase();
        if (!user.getEmail().equalsIgnoreCase(newEmail)) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("An account with this email already exists");
            }
            user.setEmail(newEmail);
        }

        user.setName(request.getName().trim());

        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isEmpty()) {
                throw new IllegalArgumentException("Current password is required to set a new password");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            if (request.getNewPassword().trim().length() < 6) {
                throw new IllegalArgumentException("New password must be at least 6 characters");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword().trim()));
        }

        user = userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());
        UserResponse userResponse = new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());

        return new AuthResponse(token, userResponse);
    }

    @Transactional
    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}
