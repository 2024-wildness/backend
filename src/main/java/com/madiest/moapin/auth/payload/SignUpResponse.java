package com.madiest.moapin.auth.payload;

import lombok.Getter;

/**
 * Response payload for user registration.
 */
@Getter
public class SignUpResponse {

    private String message;

    public SignUpResponse(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}