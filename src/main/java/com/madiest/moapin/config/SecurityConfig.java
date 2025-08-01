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
     * 주어진 AuthenticationConfiguration에서 AuthenticationManager를 반환합니다.
     *
     * @param authConfig 인증 매니저 구성을 제공하는 AuthenticationConfiguration 객체
     * @return AuthenticationManager 인스턴스
     * @throws Exception 인증 매니저를 가져오는 중 오류가 발생할 경우
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    /**
     * 애플리케이션의 JWT 인코딩을 위한 JwtEncoder 빈을 생성합니다.
     *
     * @param key 애플리케이션 프로퍼티에서 주입된 JWT 비밀 키
     * @return NimbusJwtEncoder 인스턴스
     */
    @Bean
    public JwtEncoder jwtEncoder(@Value("${jwt.secret-key}") SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    /**
     * 애플리케이션의 JWT 토큰 검증을 위해 비밀 키로 구성된 JwtDecoder 빈을 생성합니다.
     *
     * @param key 애플리케이션 프로퍼티에서 주입된 JWT 비밀 키
     * @return 비밀 키로 초기화된 JwtDecoder 인스턴스
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret-key}") SecretKey key) {
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * 시스템 UTC 시간대를 사용하는 {@link Clock} 빈을 제공합니다.
     *
     * 이 빈을 통해 서비스에서 시간 정보를 주입받아 테스트 용이성을 높이고, Clock 빈이 없을 때 발생할 수 있는 {@code NoSuchBeanDefinitionException}을 방지할 수 있습니다.
     *
     * @return 시스템 UTC 기준의 Clock 인스턴스
     */
    @Bean
    public Clock clock() {
        // Use UTC to avoid surprises with daylight‑saving changes
        return Clock.systemUTC();
    }
}