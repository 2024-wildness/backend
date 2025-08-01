package com.madiest.moapin.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * Response payload containing the JWT access token.
 */
@Setter
@Getter
public class LoginResponse {

    private String accessToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}