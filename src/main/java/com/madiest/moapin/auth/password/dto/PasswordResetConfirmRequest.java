package com.madiest.moapin.auth.password.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetConfirmRequest {
    private String token;
    private String newPassword;
}
