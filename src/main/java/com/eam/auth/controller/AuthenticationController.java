package com.eam.auth.controller;

import com.eam.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication & OTP Management", description = "APIs for user authentication, OTP generation, verification, and resend functionality")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestParam LoginRequest loginRequest) {
        return ResponseEntity.ok(new JwtResponse(authService.login(loginRequest.username, loginRequest.password)));
    }

    @PostMapping("/otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(authService.generateOtp(phoneNumber));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password", description = "Initiates password reset process by sending reset email")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset email sent if user exists");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password", description = "Resets user password using reset token")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully");
    }

    // OTP endpoints
    @PostMapping("/otp/generate")
    public ResponseEntity<OtpResponse> generateOtp(
            @Parameter(description = "Phone number for OTP generation", required = true)
            @RequestBody OtpRequest request) {
        // Validate request
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new OtpResponse(null, "Phone number is required", false));
        }

        // Check if OTP exists and is older than 30 seconds
        if (authService.isOtpExists(request.getPhoneNumber())) {
            if (authService.isOtpOlderThanThirtySeconds(request.getPhoneNumber())) {
                // OTP exists but is older than 30 seconds, regenerate it
                String otp = authService.generateOtp(request.getPhoneNumber());
                return ResponseEntity.ok(new OtpResponse(otp, "OTP regenerated successfully", true));
            } else {
                // OTP is still fresh (less than 30 seconds old)
                return ResponseEntity.badRequest()
                        .body(new OtpResponse(null, "OTP is still fresh. Please wait 30 seconds or use resend endpoint.", false));
            }
        }

        // No OTP exists, generate new one
        String otp = authService.generateOtp(request.getPhoneNumber());
        return ResponseEntity.ok(new OtpResponse(otp, "OTP generated successfully", true));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<OtpVerificationResponse> verifyOtp(
            @Parameter(description = "Phone number and OTP for verification", required = true)
            @RequestBody OtpVerificationRequest request) {
        // Validate request
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new OtpVerificationResponse(null, "Phone number is required"));
        }

        if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new OtpVerificationResponse(null, "OTP is required"));
        }

        // Verify OTP using service
        String jwt = authService.verifyOtp(request.getPhoneNumber(), request.getOtp());

        if (!"false".equals(jwt)) {
            return ResponseEntity.ok(new OtpVerificationResponse(jwt, "Verification Successful"));
        } else {
            return ResponseEntity.ok(new OtpVerificationResponse(null, "Invalid or expired OTP"));
        }
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<OtpResponse> resendOtp(
            @Parameter(description = "Phone number for OTP resend", required = true)
            @RequestBody OtpRequest request) {
        // Validate request
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new OtpResponse(null, "Phone number is required", false));
        }

        // Check if OTP is older than 30 seconds
        if (authService.isOtpOlderThanThirtySeconds(request.getPhoneNumber())) {
            // OTP is older than 30 seconds or doesn't exist, generate new one
            authService.generateOtp(request.getPhoneNumber());
            return ResponseEntity.ok(new OtpResponse(null, "New OTP generated successfully", true));
        } else {
            // OTP is still fresh (less than 30 seconds old), keep existing one
            return ResponseEntity.ok(new OtpResponse(null, "OTP is still fresh, no regeneration needed", true));
        }
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class JwtResponse {
        private String token;
    }

    @Data
    static class ResetPasswordRequest {
        private String token;
        private String newPassword;
    }

    // OTP DTOs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Request model for OTP generation and resend")
    public static class OtpRequest {
        @Schema(description = "Phone number in international format", example = "+1234567890")
        private String phoneNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Response model for OTP generation and resend")
    public static class OtpResponse {
        @Schema(description = "Generated 6-digit OTP", example = "123456")
        private String otp;
        @Schema(description = "Response message", example = "OTP generated successfully")
        private String message;
        @Schema(description = "Operation success status", example = "true")
        private boolean success;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Request model for OTP verification")
    public static class OtpVerificationRequest {
        @Schema(description = "Phone number in international format", example = "+1234567890")
        private String phoneNumber;
        @Schema(description = "6-digit OTP to verify", example = "123456")
        private String otp;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Response model for OTP verification")
    public static class OtpVerificationResponse {
        @Schema(description = "JWT Token", example = "true")
        private String token;
        @Schema(description = "Verification message", example = "OTP verified successfully")
        private String message;
    }

}