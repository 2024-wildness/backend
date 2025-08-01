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

    /**
     * JwtService의 인스턴스를 생성하여 JWT 인코딩, 디코딩, 만료 시간 및 시간 소스를 초기화합니다.
     *
     * @param encoder JWT 토큰을 인코딩하는 인코더
     * @param decoder JWT 토큰을 디코딩하는 디코더
     * @param props JWT 만료 시간을 포함하는 애플리케이션 설정
     * @param clock 현재 시간을 제공하는 Clock 인스턴스
     */
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
     * 주어진 사용자 이름을 기반으로 서명된 JWT 토큰을 생성합니다.
     *
     * @param username JWT의 subject로 설정될 사용자 이름
     * @return 생성된 JWT 토큰 문자열
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

    /**
     * 주어진 JWT 토큰이 유효한지 검사합니다.
     *
     * @param token 검증할 JWT 토큰 문자열
     * @return 토큰이 유효하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean validateToken(String token) {
        try {
            decoder.decode(token); // signature, expiry, etc.
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 주어진 JWT 토큰을 디코딩하여 클레임 정보를 포함한 {@link Jwt} 객체를 반환합니다.
     *
     * @param token 디코딩할 JWT 토큰 문자열
     * @return 토큰의 클레임과 메타데이터가 포함된 {@link Jwt} 객체
     */
    public Jwt decode(String token) {
        return decoder.decode(token);
    }
}