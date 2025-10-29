package com.planova.server.global.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planova.server.global.api.Api;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.jwt.JwtAuthenticationFilter;
import com.planova.server.global.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final ObjectMapper objectMapper;
  private final JwtProvider jwtProvider;

  Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/auth/verify-token", "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**")
            .permitAll()
            .anyRequest().authenticated())
        .httpBasic(HttpBasicConfigurer::disable)
        .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, objectMapper),
            UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exceptionHandling -> exceptionHandling

            .authenticationEntryPoint((request, response, authException) -> {
              LOGGER.warn("인증 실패 PATH: {}", request.getRequestURI());

              response.setStatus(ErrorCode.UNAUTHORIZED.getHttpStatusCode());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.setCharacterEncoding("UTF-8");
              try {

                Api<Object> errorApi = Api.ERROR(ErrorCode.UNAUTHORIZED);
                String errorResponseJson = objectMapper.writeValueAsString(errorApi);
                response.getWriter().write(errorResponseJson);
              } catch (IOException e) {
                LOGGER.error("인증 실패 응답 중 IOException 발생: {}", e.getMessage(), e);
                Api<Object> serverErrorApi = Api.ERROR(ErrorCode.SERVER_ERROR, request.getRequestURI());
                try {
                  response.getWriter().write(objectMapper.writeValueAsString(serverErrorApi));
                } catch (IOException innerE) {
                  LOGGER.error("최후의 응답 작성 실패: {}", innerE.getMessage(), innerE);
                }
              }
            })

            .accessDeniedHandler((request, response, accessDeniedException) -> {
              LOGGER.warn("접근 거부 PATH: {}", request.getRequestURI());

              response.setStatus(ErrorCode.FORBIDDEN.getHttpStatusCode());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.setCharacterEncoding("UTF-8");

              try {
                Api<Object> errorApi = Api.ERROR(ErrorCode.FORBIDDEN, request.getRequestURI());
                String errorResponseJson = objectMapper.writeValueAsString(errorApi);
                response.getWriter().write(errorResponseJson);
              } catch (IOException e) {
                LOGGER.error("접근 거부 응답 중 IOException 발생: {}", e.getMessage(), e);
                Api<Object> serverErrorApi = Api.ERROR(ErrorCode.SERVER_ERROR, request.getRequestURI());
                try {
                  response.getWriter().write(objectMapper.writeValueAsString(serverErrorApi));
                } catch (IOException innerE) {
                  LOGGER.error("최후의 응답 작성 실패: {}", innerE.getMessage(), innerE);
                }
              }
            }));
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}