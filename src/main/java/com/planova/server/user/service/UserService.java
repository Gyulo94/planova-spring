package com.planova.server.user.service;

import java.util.UUID;

import com.planova.server.auth.request.SocialLoginRequest;
import com.planova.server.user.entity.User;
import com.planova.server.user.request.ResetPasswordRequest;
import com.planova.server.user.request.UserPasswordRequest;
import com.planova.server.user.request.UserRequest;
import com.planova.server.user.request.UserUpdateRequest;
import com.planova.server.user.response.UserResponse;

public interface UserService {

  /**
   * 회원가입
   * 
   * @param UserRequest (String email, String name, String token,
   *                    String password)
   * @return UserResponse (UUID id, String name, String email, String provider,
   *         LocalDateTime createdAt)
   */
  UserResponse signup(UserRequest request);

  /**
   * 이메일로 회원 정보 조회
   * 
   * @param String (String email)
   * @return User (UUID id, String name, String email, String provider,
   *         LocalDateTime createdAt)
   */
  User findByEmail(String email);

  /**
   * 비밀번호 재설정
   * 
   * @param ResetPasswordRequest (String email, String token, String password)
   * @return String (String message)
   *         - message: "비밀번호가 성공적으로 변경되었습니다. 다시 로그인을 해주세요."
   */
  String resetPassword(ResetPasswordRequest request);

  /**
   * 유저 엔터티 반환 메서드 (공개)
   */
  User getUserEntityById(UUID id);

  /**
   * 소셜 로그인 회원 등록
   * 
   * @param SocialLoginRequest (String email, String password, String name, String
   *                           image, String provider)
   * @return LoginResponse (user: UserResponse, serverTokens: TokenResponse)
   *         - user: UserResponse (UUID id, String name, String email, String
   *         image, String provider, LocalDateTime createdAt)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  void socialSignup(SocialLoginRequest request);

  UserResponse findUserById(UUID id);

  UserResponse updateUser(UUID id, UserUpdateRequest request);

  UserResponse updateUserPassword(UUID id, UserPasswordRequest request);

  void deleteUser(UUID id);
}
