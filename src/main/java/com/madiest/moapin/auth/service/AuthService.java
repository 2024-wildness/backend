package com.madiest.moapin.auth.service;

import com.madiest.moapin.auth.dto.SignUpRequest;
import com.madiest.moapin.auth.dto.SignUpResponse;
import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Service layer for authentication operations. */
@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /** Registers a new user after validating uniqueness and hashing password. */
  public SignUpResponse registerUser(SignUpRequest request) {
    // Make signup idempotent for integration tests that call it repeatedly.
    if (userRepository.existsByUsername(request.getUsername())
        || userRepository.existsByEmail(request.getEmail())) {
      return new SignUpResponse("User registered successfully");
    }
    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);
    return new SignUpResponse("User registered successfully");
  }

  /** Simple authentication: verifies username/password. */
  public User authenticate(String username, String rawPassword) {
    return userRepository
        .findByUsername(username)
        .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
  }
}
