package com.eam.auth.service;

import com.eam.auth.model.PasswordResetToken;
import com.eam.auth.model.User;
import com.eam.auth.repository.PasswordResetTokenRepository;
import com.eam.auth.repository.UserRepository;
import com.eam.auth.utils.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    // OTP related fields
    private final SecureRandom random = new SecureRandom();
    private final Map<String, OtpData> otpCache = new ConcurrentHashMap<>();
    private static final int OTP_EXPIRY_MINUTES = 5;

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        return jwtService.generateJwtToken(username);
    }

    public String loginWithPhoneNumberAndOTP(String username, String password) {
        return jwtService.generateJwtToken(username);
    }

    // OTP Methods
    @Transactional
    public String generateOtp(String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);

        // Generate 6-digit OTP
        String otp = generateSixDigitOtp();

        // Store OTP with expiration time
        OtpData otpData = new OtpData(otp, LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        otpCache.put(phoneNumber, otpData);

        log.info("Generated OTP {} for phone number: {}", otpData.getOtp(), phoneNumber);

        // TODO: In production, send OTP via SMS/email service here

        return "OTP Generated Successfully: "+otp;
    }

    public String verifyOtp(String phoneNumber, String otp) {
        OtpData otpData = otpCache.get(phoneNumber);

        if (otpData == null) {
            log.warn("No OTP found for phone number: {}", phoneNumber);
            return "false";
        }

        // Check if OTP is expired
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            log.warn("OTP expired for phone number: {}", phoneNumber);
            otpCache.remove(phoneNumber); // Clean up expired OTP
            return "false";
        }

        // Verify OTP
        boolean isValid = otpData.getOtp().equals(otp);

        if (isValid) {
            User user = userService.findByPhoneNumber(phoneNumber);
            log.info("OTP verified successfully for phone number: {}", phoneNumber);
            // Remove OTP from cache after successful verification
            otpCache.remove(phoneNumber);
            return jwtService.generateJwtToken(user.getUsername());
        } else {
            log.warn("Invalid OTP provided for phone number: {}", phoneNumber);
        }

        return "false";
    }

    public boolean isOtpExists(String phoneNumber) {
        OtpData otpData = otpCache.get(phoneNumber);
        if (otpData == null) {
            return false;
        }

        // Check if OTP is expired
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpCache.remove(phoneNumber); // Clean up expired OTP
            return false;
        }

        return true;
    }

    public boolean isOtpOlderThanThirtySeconds(String phoneNumber) {
        OtpData otpData = otpCache.get(phoneNumber);
        if (otpData == null) {
            return true; // No OTP exists, consider it as "older" to allow generation
        }

        // Check if OTP is expired
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpCache.remove(phoneNumber); // Clean up expired OTP
            return true; // Expired OTP is considered "older"
        }

        // Check if OTP was created more than 30 seconds ago
        // We need to calculate the time difference from creation, not expiry
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
        LocalDateTime creationTime = otpData.getExpiryTime().minusMinutes(OTP_EXPIRY_MINUTES);
        return creationTime.isBefore(thirtySecondsAgo);
    }

    public void resendOtp(String phoneNumber) {
        OtpData existingOtp = otpCache.get(phoneNumber);

        // Check if OTP exists and is older than 30 seconds
        if (existingOtp != null) {
            LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
            LocalDateTime creationTime = existingOtp.getExpiryTime().minusMinutes(OTP_EXPIRY_MINUTES);

            if (creationTime.isAfter(thirtySecondsAgo)) {
                // OTP is still fresh (less than 30 seconds old), don't regenerate
                log.info("OTP for phone number {} is still fresh (less than 30 seconds old), not regenerating", phoneNumber);
                return;
            }
        }

        // Remove existing OTP if any
        otpCache.remove(phoneNumber);

        // Generate new OTP
        generateOtp(phoneNumber);

        log.info("OTP regenerated for phone number: {}", phoneNumber);
    }

    private String generateSixDigitOtp() {
        // Generate a random 6-digit number
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userService.findByEmail(email);

        // Remove old token if any
        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600)) // 1 hour expiry
                .build();

        passwordResetTokenRepository.save(resetToken);

        // Send email with reset link (stubbed)
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Expired password reset token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user.getId(), user);

        passwordResetTokenRepository.delete(resetToken);
    }

    // Inner class to store OTP data with expiration
    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}