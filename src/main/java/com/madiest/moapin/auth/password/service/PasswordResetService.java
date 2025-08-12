package com.madiest.moapin.auth.password.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {

    /**
     * Initiates the password reset process for a user.
     * Generates a unique, secure token and sends it to the user's email.
     *
     * @param email The email of the user requesting a password reset.
     */
    @Transactional
    public void initiatePasswordReset(String email) {
        // TODO: 1. Find user by email.
        // TODO: 2. Generate a secure, single-use token with an expiration date.
        // TODO: 3. Save the token to the database, associated with the user.
        // TODO: 4. Send an email to the user with the password reset link (containing the token) via AWS SES.
    }

    /**
     * Completes the password reset process.
     * Validates the provided token and updates the user's password.
     *
     * @param token The password reset token.
     * @param newPassword The new password for the user.
     */
    @Transactional
    public void completePasswordReset(String token, String newPassword) {
        // TODO: 1. Find the token in the database.
        // TODO: 2. Validate the token (check if it exists, is not expired, and has not been used).
        // TODO: 3. If valid, find the associated user.
        // TODO: 4. Update the user's password with the new, securely hashed password.
        // TODO: 5. Mark the token as used to prevent reuse.
    }
}