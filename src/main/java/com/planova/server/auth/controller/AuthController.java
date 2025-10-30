package com.planova.server.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.auth.request.LoginRequest;
import com.planova.server.auth.request.SocialLoginRequest;
import com.planova.server.auth.response.LoginResponse;
import com.planova.server.auth.response.TokenResponse;
import com.planova.server.auth.service.AuthService;
import com.planova.server.email.request.EmailRequest;
import com.planova.server.email.service.EmailService;
import com.planova.server.global.api.Api;
import com.planova.server.global.message.ResponseMessage;
import com.planova.server.user.request.ResetPasswordRequest;
import com.planova.server.user.request.UserRequest;
import com.planova.server.user.response.UserResponse;
import com.planova.server.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@SecurityRequirements(value = {})
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {
  Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  private final UserService userService;
  private final EmailService emailService;
  private final AuthService authService;

  @PostMapping("signup")
  @Operation(summary = "회원가입", description = "이메일, 이름, 비밀번호, 이메일 인증 토큰을 받아 회원가입을 진행합니다.")
  public Api<UserResponse> signup(@Valid @RequestBody UserRequest request) {
    UserResponse response = userService.signup(request);
    return Api.OK(response);
  }

  @PostMapping("login")
  @Operation(summary = "로그인", description = "이메일과 비밀번호를 받아 로그인을 진행합니다.")
  public Api<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return Api.OK(response);
  }

  @PostMapping("social-login")
  @Operation(summary = "소셜 로그인", description = "소셜 로그인 정보를 (NextAuth로 부터) 받아 로그인을 진행합니다.")
  public Api<LoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
    LoginResponse response = authService.socialLogin(request);
    return Api.OK(response);
  }

  @PostMapping("refresh")
  @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 받아 새로운 액세스 토큰과 리프레시 토큰을 발급합니다.")
  public Api<TokenResponse> refresh(@RequestHeader("Authorization") String authorization) {
    TokenResponse response = authService.refresh(authorization);
    return Api.OK(response);
  }

  @PostMapping("/reset-password")
  @Operation(summary = "비밀번호 재설정", description = "이메일, 이메일 인증 토큰, 새 비밀번호를 받아 비밀번호 재설정을 진행합니다.")
  public Api<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    userService.resetPassword(request);
    return Api.OK(ResponseMessage.RESET_PASSWORD_SUCCESS);
  }

  @PostMapping("/send-signup-email")
  @Operation(summary = "회원가입 이메일 인증", description = "회원가입을 위한 이메일 인증 링크를 전송합니다.")
  public Api<String> sendSignupEmail(@Valid @RequestBody EmailRequest request) throws MessagingException {
    emailService.sendVerificationMail(request);
    return Api.OK(ResponseMessage.SEND_EMAIL_SUCCESS);
  }

  @PostMapping("/send-reset-password-email")
  @Operation(summary = "비밀번호 재설정 이메일 인증", description = "비밀번호 재설정을 위한 이메일 인증 링크를 전송합니다.")
  public Api<String> sendResetPasswordEmail(@Valid @RequestBody EmailRequest request) throws MessagingException {
    String mesage = emailService.sendVerificationMail(request);
    return Api.OK(mesage);
  }

  @GetMapping("/verify-token")
  @Operation(summary = "이메일 인증 토큰 확인", description = "이메일 인증 토큰을 확인하고, 유효한 경우 해당 이메일을 반환합니다.")
  public Api<String> verifyToken(@RequestParam("token") String token) {
    String email = emailService.verifyToken(token);
    return Api.OK(email);
  }
}
