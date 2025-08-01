package com.madiest.moapin.auth;

import com.madiest.moapin.config.AppProperties;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * JWT utility that leverages Spring Security’s {@link JwtEncoder}/{@link JwtDecoder}
 * instead of direct JJWT usage. This means:
 * <ul>
 *   <li>Token 검증은 Spring Security 필터 체인과 동일한 로직(Nimbus)으로 수행</li>
 *   <li>발급 역시 {@link JwtEncoder}로 구현해 코드 일관성 유지</li>
 *   <li>테스트가 쉬운 {@link Clock} 주입</li>
 *   <li>만료 시간은 {@link Duration} 형태로 <code>application.yml</code>에서 설정</li>
 * </ul>
 */
@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration expiration;
    private final Clock clock;

    public JwtService(JwtEncoder encoder,
                      JwtDecoder decoder,
                      AppProperties props,
                      Clock clock) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.expiration = props.getJwt().getExpiration();
        this.clock = clock;
    }

    /**
     * Generates a signed JWT for the supplied username.
     */
    public String generateToken(String username) {
        Instant now = clock.instant();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plus(expiration))
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /** Delegates validation to Spring Security’s {@link JwtDecoder}. */
    public boolean validateToken(String token) {
        try {
            decoder.decode(token); // signature, expiry, etc.
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Convenience accessor for downstream layers that need claims.
     */
    public Jwt decode(String token) {
        return decoder.decode(token);
    }
}