package com.madiest.moapin.auth.filter;

import com.madiest.moapin.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.stereotype.Component;

/**
 * Filter to authenticate requests via JWT tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * JwtAuthenticationFilter를 지정된 JwtService 인스턴스로 초기화합니다.
     *
     * @param jwtService JWT 토큰의 검증 및 디코딩에 사용되는 서비스
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * JWT 토큰을 이용해 HTTP 요청을 인증하고, 인증 정보를 보안 컨텍스트에 설정합니다.
     *
     * 요청에서 JWT 토큰을 추출하고, 유효한 경우 토큰의 subject를 기반으로 인증 객체를 생성하여 SecurityContextHolder에 저장합니다.
     * 인증이 완료된 후 필터 체인을 계속 진행합니다.
     *
     * @param request  인증할 HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 처리 중 예외가 발생한 경우
     * @throws IOException 입출력 오류가 발생한 경우
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = parseJwt(request);
        if (jwt != null && jwtService.validateToken(jwt)) {
            Jwt decoded = jwtService.decode(jwt);
            String username = decoded.getSubject();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.emptyList());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}