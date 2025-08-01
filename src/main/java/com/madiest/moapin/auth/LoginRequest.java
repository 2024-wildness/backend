package com.madiest.moapin.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request payload for user login.
 */
public class LoginRequest {

    @NotBlank
    @Getter
    private String username;

    @Setter
    @Getter
    @NotBlank
    private String password;

}