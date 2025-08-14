package com.eam.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendPasswordResetEmail(String toEmail, String token) {
        // Here you integrate with SMTP or external email provider
        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        log.info("Sending password reset email to {} with link: {}", toEmail, resetUrl);
        // You can replace the log with actual email sending code using JavaMailSender
    }
}