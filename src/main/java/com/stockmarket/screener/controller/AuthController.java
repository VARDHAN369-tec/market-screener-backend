package com.stockmarket.screener.controller;

import com.stockmarket.screener.dto.*;
import com.stockmarket.screener.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal Object principal) {
        String email = extractEmail(principal);
        if (email == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized", "UNAUTHORIZED"));
        }
        UserResponse user = authService.getCurrentUser(email);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping({"/profile", "/me"})
    public ResponseEntity<ApiResponse<AuthResponse>> updateProfile(
            @AuthenticationPrincipal Object principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        String email = extractEmail(principal);
        if (email == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized", "UNAUTHORIZED"));
        }
        AuthResponse response = authService.updateProfile(email, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }

    @DeleteMapping({"/profile", "/me"})
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@AuthenticationPrincipal Object principal) {
        String email = extractEmail(principal);
        if (email == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized", "UNAUTHORIZED"));
        }
        authService.deleteAccount(email);
        return ResponseEntity.ok(ApiResponse.success(null, "Account successfully deleted"));
    }

    private String extractEmail(Object principal) {
        if (principal == null || "anonymousUser".equals(principal)) {
            return null;
        }
        if (principal instanceof com.stockmarket.screener.entity.User u) {
            return u.getEmail();
        } else if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        } else if (principal instanceof String s) {
            return s;
        }
        return null;
    }
}
