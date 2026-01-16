package com. denso.anomaly_training_backend.controller;

import com. denso.anomaly_training_backend.dto.request.LoginRequest;
import com.denso. anomaly_training_backend.dto.request.RefreshTokenRequest;
import com. denso.anomaly_training_backend.dto.request.RegisterRequest;
import com.denso. anomaly_training_backend.dto.response.ApiResponse;
import com.denso.anomaly_training_backend. dto.response.AuthResponse;
import com.denso.anomaly_training_backend.dto.response.UserResponse;
import com.denso.anomaly_training_backend.exception.AuthException;
import com. denso.anomaly_training_backend.model.User;
import com.denso.anomaly_training_backend.repository.UserRepository;
import com. denso.anomaly_training_backend.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework. http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security. core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * Register a new local user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("Register request for username: {}", request.getUsername());

        AuthResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
    }

    /**
     * Login with username and password
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        log.info("Login request for username: {}", request. getUsername());

        AuthResponse response = authService.login(request);

        return ResponseEntity
                .ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Refresh access token using refresh token
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request");

        AuthResponse response = authService.refreshToken(request);

        return ResponseEntity
                .ok(ApiResponse.success("Token refreshed successfully", response));
    }

    /**
     * Logout - revoke refresh token
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("Logout request");

        authService.logout(request.getRefreshToken());

        return ResponseEntity
                .ok(ApiResponse.success("Logout successful", null));
    }

    /**
     * Logout from all devices - revoke all refresh tokens
     * POST /api/auth/logout-all
     */
    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Logout all request for user: {}", userDetails.getUsername());

        authService.logoutAll(userDetails. getUsername());

        return ResponseEntity
                .ok(ApiResponse.success("Logged out from all devices", null));
    }

    /**
     * Get current authenticated user info
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsernameAndDeleteFlagFalse(userDetails.getUsername())
                .orElseThrow(() -> new AuthException("User not found"));

        return ResponseEntity
                .ok(ApiResponse.success(UserResponse.fromEntity(user)));
    }
}