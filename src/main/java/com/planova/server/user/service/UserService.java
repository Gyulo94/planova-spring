package com.planova.server.user.service;

import java.util.UUID;

import com.planova.server.user.entity.User;
import com.planova.server.user.request.UserRequest;
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
   * 유저 엔터티 반환 메서드 (공개)
   */
  User getUserEntityById(UUID id);
}
