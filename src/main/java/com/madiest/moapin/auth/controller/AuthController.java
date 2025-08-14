package com.madiest.moapin.auth.controller;

import com.madiest.moapin.auth.dto.LoginRequest;
import com.madiest.moapin.auth.dto.LoginResponse;
import com.madiest.moapin.auth.dto.SignUpRequest;
import com.madiest.moapin.auth.dto.SignUpResponse;
import com.madiest.moapin.auth.service.AuthService;
import com.madiest.moapin.auth.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for authentication endpoints. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  private final JwtService jwtService;

  public AuthController(AuthService authService, JwtService jwtService) {
    this.authService = authService;
    this.jwtService = jwtService;
  }

  @PostMapping("/signup")
  public ResponseEntity<SignUpResponse> registerUser(@Valid @RequestBody SignUpRequest request) {
    SignUpResponse response = authService.registerUser(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
    var user = authService.authenticate(request.getUsername(), request.getPassword());
    String token = jwtService.generateToken(user.getUsername());
    return ResponseEntity.ok(new LoginResponse(token));
  }
}
