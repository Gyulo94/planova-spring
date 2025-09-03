package com.planova.server.email.service;

import com.planova.server.email.request.EmailRequest;

import jakarta.mail.MessagingException;

public interface EmailService {
  /**
   * 이메일 인증
   * 
   * @param EmailRequest (String email, String type)
   * @return String (String message)
   *         - message: "이메일을 성공적으로 보냈습니다. 링크를 통해 진행해주세요."
   */
  String sendVerificationMail(EmailRequest request) throws MessagingException;

  /**
   * 이메일 인증 토큰 확인
   * 
   * @param String (String token)
   * @return String (String email)
   */
  String verifyToken(String token);
}
