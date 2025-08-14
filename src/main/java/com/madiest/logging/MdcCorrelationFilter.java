package com.madiest.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Populates MDC with a per-request correlation ID and (if authenticated) a userId. */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class MdcCorrelationFilter extends OncePerRequestFilter {

  private static final String HDR_REQUEST_ID = "X-Request-Id";
  private static final String MDC_REQUEST_ID = "requestId";
  private static final String MDC_USER_ID = "userId";

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String requestId = resolveRequestId(request);
    MDC.put(MDC_REQUEST_ID, requestId);
    response.setHeader(HDR_REQUEST_ID, requestId);

    // Attempt to extract user identifier from SecurityContext (placeholder: principal name)
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated() && auth.getPrincipal() != null) {
      String userId = auth.getName(); // Replace with domain user id once implemented
      MDC.put(MDC_USER_ID, userId);
    }
    try {
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(MDC_REQUEST_ID);
      MDC.remove(MDC_USER_ID);
    }
  }

  private String resolveRequestId(HttpServletRequest request) {
    String existing = request.getHeader(HDR_REQUEST_ID);
    if (existing != null && !existing.isBlank()) {
      return existing;
    }
    return UUID.randomUUID().toString();
  }
}
