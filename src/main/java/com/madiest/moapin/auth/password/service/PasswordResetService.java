package com.madiest.moapin.auth.password.service;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.auth.password.model.PasswordResetToken;
import com.madiest.moapin.auth.password.repository.PasswordResetTokenRepository;
import com.madiest.moapin.auth.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

  private final UserRepository userRepository;
  private final PasswordResetTokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Initiates the password reset process for a user. Generates a unique, secure token and sends it
   * to the user's email.
   *
   * @param email The email of the user requesting a password reset.
   */
  @Transactional
  public void initiatePasswordReset(String email) {
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      // For security, don't reveal whether email exists or not
      return;
    }

    User user = userOpt.get();

    // Clean up any existing tokens for this user
    tokenRepository.findAll().stream()
        .filter(token -> token.getUser().getId().equals(user.getId()))
        .forEach(tokenRepository::delete);

    // Generate a new token
    String tokenValue = UUID.randomUUID().toString();
    PasswordResetToken token =
        new PasswordResetToken(tokenValue, user, LocalDateTime.now().plusHours(1));

    tokenRepository.save(token);

    // TODO: Send email with token (for now, just store the token)
  }

  /**
   * Completes the password reset process. Validates the provided token and updates the user's
   * password.
   *
   * @param token The password reset token.
   * @param newPassword The new password for the user.
   */
  @Transactional
  public void completePasswordReset(String token, String newPassword) {
    Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

    if (tokenOpt.isEmpty()) {
      throw new IllegalArgumentException("Invalid token");
    }

    PasswordResetToken resetToken = tokenOpt.get();

    // Check if token is expired or used
    if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Token is expired or already used");
    }

    User user = resetToken.getUser();

    // Update password
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    // Mark token as used
    resetToken.setUsed(true);
    tokenRepository.save(resetToken);
  }
}
