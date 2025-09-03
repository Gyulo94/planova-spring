package com.planova.server.auth.service;

import com.planova.server.auth.request.LoginRequest;
import com.planova.server.auth.response.LoginResponse;
import com.planova.server.auth.response.TokenResponse;

public interface AuthService {

  /**
   * 로그인
   * 
   * @param reuqest 로그인 요청 (String email, String password)
   * @return 회원 세션정보 응답 (user: UserResponse, serverTokens: TokenResponse)
   *         - user: UserResponse (UUID id, String name, String email, String
   *         image, Role role, String provider, LocalDateTime createdAt)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  LoginResponse login(LoginRequest request);

  /**
   * 리프레시 토큰으로 액세스 토큰 재발급
   * 
   * @param authorization Authorization 헤더에 포함된 리프레시 토큰
   * @return TokenResponse (serverTokens: TokenResponse)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  TokenResponse refresh(String authorization);

}
