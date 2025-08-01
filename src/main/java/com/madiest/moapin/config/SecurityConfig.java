package com.madiest.moapin.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import java.time.Clock;

/**
 * Security configuration beans.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager를 반환합니다.
     *
     * AuthenticationConfiguration에서 AuthenticationManager를 가져와 반환합니다.
     *
     * @param authConfig AuthenticationManager를 제공하는 설정 객체
     * @return AuthenticationManager 인스턴스
     * @throws Exception AuthenticationManager를 가져올 수 없는 경우 발생
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    /**
     * 애플리케이션의 비밀 키를 사용하여 JWT를 인코딩하는 JwtEncoder 빈을 생성합니다.
     *
     * @param key 애플리케이션 설정에서 주입된 JWT 비밀 키
     * @return NimbusJwtEncoder 인스턴스
     */
    @Bean
    public JwtEncoder jwtEncoder(@Value("${jwt.secret-key}") SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    /**
     * 애플리케이션의 비밀 키를 사용하여 JWT 토큰을 검증하는 NimbusJwtDecoder 빈을 생성합니다.
     *
     * @param key 애플리케이션 설정에서 주입된 JWT 비밀 키
     * @return JWT 토큰 디코딩에 사용되는 JwtDecoder 인스턴스
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret-key}") SecretKey key) {
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /****
     * 시스템 UTC 시간대를 사용하는 {@link Clock} 빈을 제공합니다.
     *
     * 이 빈을 통해 서비스에서 테스트에 적합한 방식으로 시간을 주입할 수 있으며, {@code NoSuchBeanDefinitionException}을 방지할 수 있습니다.
     * 반환되는 Clock은 항상 UTC 기준의 시스템 시계를 나타냅니다.
     *
     * @return UTC 시스템 시계 {@link Clock}
     */
    @Bean
    public Clock clock() {
        // Use UTC to avoid surprises with daylight‑saving changes
        return Clock.systemUTC();
    }
}