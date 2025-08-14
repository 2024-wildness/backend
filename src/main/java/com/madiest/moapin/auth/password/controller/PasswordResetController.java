package com.madiest.moapin.auth.password.controller;

import com.madiest.moapin.auth.password.dto.PasswordResetRequest;
import com.madiest.moapin.auth.password.dto.PasswordUpdateRequest;
import com.madiest.moapin.auth.password.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @PostMapping("/reset-request")
  public ResponseEntity<Void> requestPasswordReset(@RequestBody PasswordResetRequest request) {
    passwordResetService.initiatePasswordReset(request.getEmail());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset")
  public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdateRequest request) {
    passwordResetService.completePasswordReset(request.getToken(), request.getNewPassword());
    return ResponseEntity.ok().build();
  }
}
