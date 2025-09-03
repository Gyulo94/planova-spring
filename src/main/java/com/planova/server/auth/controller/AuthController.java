package com.planova.server.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.email.request.EmailRequest;
import com.planova.server.email.service.EmailService;
import com.planova.server.global.api.Api;
import com.planova.server.global.message.ResponseMessage;
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
