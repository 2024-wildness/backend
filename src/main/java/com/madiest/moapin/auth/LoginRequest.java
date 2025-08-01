package com.madiest.moapin.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request payload for user login.
 */
@Getter
public class LoginRequest {

    @NotBlank
    @Setter
    private String username;

    @Setter
    @NotBlank
    private String password;
}