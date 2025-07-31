package com.madiest.moapin.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for generating JSON Web Tokens.
 */
@Service
public class JwtService {

    private final String jwtSecret;
    private final long jwtExpirationMs = 86400000; // 24 hours

    public JwtService(com.madiest.moapin.config.AppProperties props) {
        this.jwtSecret = props.getJwt().getSecret();
    }

    /**
     * Create a signed JWT containing the username as subject.
     */
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }

    /**
     * Validate the JWT token signature and expiration.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parse claims from the token for authentication.
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}