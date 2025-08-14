package com.madiest.moapin.auth.service;

import com.madiest.moapin.common.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

/**
 * JWT utility that uses JJWT library directly instead of Spring Security's JWT encoder/decoder.
 * This simplifies the configuration and avoids issues with Nimbus JWT encoder configuration.
 */
@Service
public class JwtService {

  private final Duration expiration;
  private final Clock clock;
  private final SecretKey secretKey;

  /**
   * JwtService의 인스턴스를 생성하여 JWT 인코딩, 디코딩, 만료 시간 및 시간 소스를 초기화합니다.
   *
   * @param props JWT 만료 시간을 포함하는 애플리케이션 설정
   * @param clock 현재 시간을 제공하는 Clock 인스턴스
   */
  public JwtService(AppProperties props, Clock clock) {
    Duration configured = props.getJwt().getExpiration();
    // Provide a sensible default (1 hour) if not configured to keep tests and local runs working.
    this.expiration = configured != null ? configured : Duration.ofHours(1);
    this.clock = clock;
    
    // Initialize secret key
    String secret = props.getJwt().getSecretKey();
    if (secret == null || secret.isEmpty()) {
      throw new IllegalStateException("JWT secret key is not configured. Please set app.jwt.secret or app.jwt.secret-key property.");
    }
    
    try {
      byte[] keyBytes = Base64.getDecoder().decode(secret);
      this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("JWT secret key is not valid Base64: " + e.getMessage(), e);
    }
  }

  /**
   * 주어진 사용자 이름을 기반으로 서명된 JWT 토큰을 생성합니다.
   *
   * @param username JWT의 subject로 설정할 사용자 이름
   * @return 생성된 JWT 토큰 문자열
   */
  public String generateToken(String username) {
    Instant now = clock.instant();
    Date issuedAt = Date.from(now);
    Date expiresAt = Date.from(now.plus(expiration));

    return Jwts.builder()
        .subject(username)
        .issuedAt(issuedAt)
        .expiration(expiresAt)
        .signWith(secretKey)
        .compact();
  }

  /**
   * 주어진 JWT 토큰이 유효한지 검사합니다.
   *
   * @param token 검증할 JWT 토큰 문자열
   * @return 토큰이 유효하면 {@code true}, 그렇지 않으면 {@code false}
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * JWT 토큰을 디코드하여 클레임 정보를 반환합니다.
   *
   * @param token 디코드할 JWT 토큰 문자열
   * @return 토큰에서 추출한 클레임 정보
   * @throws Exception 토큰이 유효하지 않거나 디코딩에 실패한 경우 발생합니다.
   */
  public Claims decode(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * JWT 토큰에서 사용자 이름을 추출합니다.
   *
   * @param token JWT 토큰 문자열
   * @return 토큰의 subject (사용자 이름)
   */
  public String extractUsername(String token) {
    return decode(token).getSubject();
  }
}
