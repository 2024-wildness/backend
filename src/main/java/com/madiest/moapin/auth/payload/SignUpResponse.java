package com.madiest.moapin.auth.payload;

/**
 * Response payload for user registration.
 */
public class SignUpResponse {

    private String message;

    public SignUpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}