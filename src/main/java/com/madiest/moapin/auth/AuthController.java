package com.madiest.moapin.auth;

import com.madiest.moapin.auth.payload.SignUpRequest;
import com.madiest.moapin.auth.payload.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> registerUser(
            @Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}