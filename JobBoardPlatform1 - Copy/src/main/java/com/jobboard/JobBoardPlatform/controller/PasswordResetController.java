package com.jobboard.JobBoardPlatform.controller;

import com.jobboard.JobBoardPlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin for production
public class PasswordResetController {

    @Autowired
    private UserService userService;

    /**
     * Handle forgot password request
     *
     * @param payload contains the email address
     * @return response indicating success or failure
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> handleForgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required."));
        }

        if (!userService.doesEmailExist(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email address not found."));
        }

        userService.deleteExistingResetTokenByEmail(email);
        boolean emailSent = userService.sendPasswordResetEmail(email);

        return emailSent
                ? ResponseEntity.ok(Map.of("message", "A reset link has been sent to your email."))
                : ResponseEntity.badRequest().body(Map.of("error", "Failed to send email. Please try again."));
    }

    /**
     * Validate the reset token
     *
     * @param token the password reset token
     * @return response indicating if the token is valid
     */
    @GetMapping("/reset-password")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token) {
        boolean isValidToken = userService.validatePasswordResetToken(token);

        if (!isValidToken) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired password reset token."));
        }

        return ResponseEntity.ok(Map.of("message", "Token is valid."));
    }

    /**
     * Handle password reset request
     *
     * @param payload contains the token, newPassword, and confirmNewPassword
     * @return response indicating success or failure
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> handlePasswordReset(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");
        String confirmNewPassword = payload.get("confirmNewPassword");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token is required."));
        }

        if (newPassword == null || confirmNewPassword == null || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password fields cannot be empty."));
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match."));
        }

        boolean isResetSuccessful = userService.resetUserPassword(token, newPassword);

        return isResetSuccessful
                ? ResponseEntity.ok(Map.of("message", "Your password has been successfully reset."))
                : ResponseEntity.badRequest().body(Map.of("error", "Failed to reset password. Please try again."));
    }
}
