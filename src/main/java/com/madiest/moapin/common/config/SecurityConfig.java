package com.madiest.moapin.common.config;

import com.madiest.moapin.auth.filter.JwtAuthenticationFilter;
import com.madiest.moapin.auth.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Security configuration beans. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtService jwtService;

  public SecurityConfig(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtService);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                    .permitAll()
                        .requestMatchers("/favicon.ico")
                        .permitAll()
                    // Allow unauthenticated health probe (for Docker/infra liveness checks)
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/api/test/public")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    // TODO: In production profile consider restricting swagger endpoints or disabling via property:
    // springdoc.swagger-ui.enabled=false & springdoc.api-docs.enabled=false
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
