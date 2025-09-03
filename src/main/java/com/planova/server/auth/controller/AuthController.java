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

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  private final UserService userService;
  private final EmailService emailService;
  private final AuthService authService;

  /**
   * 회원가입
   * 
   * @param UserRequest (String email, String name, String token,
   *                    String password)
   * @return UserResponse (UUID id, String name, String email, String provider,
   *         LocalDateTime createdAt)
   */
  @PostMapping("signup")
  public Api<UserResponse> signup(@Valid @RequestBody UserRequest request) {
    UserResponse response = userService.signup(request);
    return Api.OK(response);
  }

  /**
   * 로그인
   * 
   * @param LoginRequest (String email, String password)
   * @return LoginResponse (user: UserResponse, serverTokens: TokenResponse)
   *         - user: UserResponse (UUID id, String name, String email, String
   *         image, Role role, String provider, LocalDateTime createdAt)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  @PostMapping("login")
  public Api<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return Api.OK(response);
  }

  /**
   * 리프레시 토큰으로 액세스 토큰 재발급
   * 
   * @param authorization Authorization 헤더에 포함된 리프레시 토큰
   * @return 새로 발급된 액세스 토큰과 리프레시 토큰 (serverTokens: TokenResponse)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  @PostMapping("refresh")
  public Api<TokenResponse> refresh(@RequestHeader("Authorization") String authorization) {
    TokenResponse response = authService.refresh(authorization);
    return Api.OK(response);
  }

  /**
   * 비밀번호 재설정
   * 
   * @param ResetPasswordRequest (String email, String token, String password)
   * @return String (String message)
   *         - message: "비밀번호가 성공적으로 변경되었습니다. 다시 로그인을 해주세요."
   */
  @PostMapping("/reset-password")
  public Api<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    userService.resetPassword(request);
    return Api.OK(ResponseMessage.RESET_PASSWORD_SUCCESS);
  }

  /**
   * 회원가입 이메일 인증
   * 
   * @param EmailRequest (String email, String type)
   * @return String (String message)
   *         - message: "이메일을 성공적으로 보냈습니다. 링크를 통해 진행해주세요."
   */
  @PostMapping("/send-signup-email")
  public Api<String> sendSignupEmail(@Valid @RequestBody EmailRequest request) throws MessagingException {
    emailService.sendVerificationMail(request);
    return Api.OK(ResponseMessage.SEND_EMAIL_SUCCESS);
  }

  /**
   * 비밀번호 재설정 이메일 인증
   * 
   * @param EmailRequest (String email, String type)
   * @return String (String message)
   *         - message: "이메일을 성공적으로 보냈습니다. 링크를 통해 진행해주세요.
   */
  @PostMapping("/send-reset-password-email")
  public Api<String> sendResetPasswordEmail(@Valid @RequestBody EmailRequest request) throws MessagingException {
    String mesage = emailService.sendVerificationMail(request);
    return Api.OK(mesage);
  }

  /**
   * 이메일 인증 토큰 확인
   * 
   * @param String (String token)
   * @return String (String email)
   */
  @GetMapping("/verify-token")
  public Api<String> verifyToken(@RequestParam("token") String token) {
    String email = emailService.verifyToken(token);
    return Api.OK(email);
  }
}
