package com.planova.server.global.jwt;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planova.server.global.api.Api;
import com.planova.server.global.error.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      String accessToken = parseBearerToken(request);

      if (accessToken == null) {
        LOGGER.debug("Authorization 헤더가 없거나 Bearer 토큰이 아닙니다. URI: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
        return;
      }

      Claims claims = null;
      try {
        claims = jwtProvider.validateAccessToken(accessToken);
      } catch (ExpiredJwtException e) {
        LOGGER.warn("액세스 토큰이 만료되었습니다. URI: {}", request.getRequestURI());
        setErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED, request.getRequestURI());
        return;
      } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
        LOGGER.warn("유효하지 않은 토큰입니다. URI: {}, 예외: {}", request.getRequestURI(), e.getClass().getSimpleName());
        setErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN, request.getRequestURI());
        return;
      }

      if (claims == null) {
        LOGGER.warn("유효하지 않은 Access Token입니다. (클레임 없음). URI: {}", request.getRequestURI());
        setErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN, request.getRequestURI());
        return;
      }

      String userId = claims.getSubject();

      if (!StringUtils.hasText(userId)) {
        LOGGER.warn("Access Token Claims에 사용자 ID가 없거나 비어있습니다. URI: {}", request.getRequestURI());
        setErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN, request.getRequestURI());
        return;
      }

      UUID id;
      try {
        id = UUID.fromString(userId);
      } catch (IllegalArgumentException e) {
        LOGGER.warn("Access Token Claims의 사용자 ID 형식이 올바르지 않습니다. (UUID 아님): {}. URI: {}", userId,
            request.getRequestURI());
        setErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN, request.getRequestURI());
        return;
      }

      AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          new JwtPayload(id),
          null,
          null);
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authenticationToken);
      SecurityContextHolder.setContext(securityContext);

      LOGGER.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", id, request.getRequestURI());

    } catch (Exception e) {
      LOGGER.error("JWT 인증 필터에서 알 수 없는 오류가 발생했습니다. URI: {}", request.getRequestURI(), e);
      setErrorResponse(response, ErrorCode.SERVER_ERROR, request.getRequestURI());
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String parseBearerToken(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (!StringUtils.hasText(authorization)) {
      LOGGER.debug("Authorization 헤더가 없습니다.");
      return null;
    }
    if (authorization.startsWith("Bearer ")) {
      return authorization.substring(7);
    }
    LOGGER.debug("Authorization 헤더가 'Bearer '로 시작하지 않습니다: {}", authorization);
    return null;
  }

  private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode, String requestURI)
      throws IOException {
    response.setStatus(errorCode.getHttpStatusCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    try {
      Api<Object> errorApi = Api.ERROR(errorCode, requestURI);
      String errorResponseJson = objectMapper.writeValueAsString(errorApi);
      response.getWriter().write(errorResponseJson);
    } catch (IOException e) {
      LOGGER.error("필터 에러 응답 작성 중 IOException 발생. URI: {}", requestURI, e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생했습니다.");
    }
  }
}