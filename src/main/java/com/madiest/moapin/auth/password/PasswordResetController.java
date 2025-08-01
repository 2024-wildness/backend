package com.madiest.moapin.auth.password;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for password reset operations.
 */
@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {
    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    public static class ResetRequest {
        @NotBlank
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class ResetConfirm {
        @NotBlank
        private String token;
        @NotBlank
        private String password;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/reset-request")
    public ResponseEntity<Void> requestReset(@Valid @RequestBody ResetRequest req) {
        service.createResetToken(req.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@Valid @RequestBody ResetConfirm req) {
        service.resetPassword(req.getToken(), req.getPassword());
        return ResponseEntity.ok().build();
    }
}
