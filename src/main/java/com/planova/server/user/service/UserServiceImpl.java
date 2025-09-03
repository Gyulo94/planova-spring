package com.planova.server.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.User;
import com.planova.server.user.repository.UserRepository;
import com.planova.server.user.request.UserRequest;
import com.planova.server.user.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, String> redisTemplate;

  /**
   * 회원가입
   * 
   * @param UserRequest (String email, String name, String token,
   *                    String password)
   * @return UserResponse (UUID id, String name, String email, String provider,
   *         LocalDateTime createdAt)
   */
  @Override
  public UserResponse signup(UserRequest request) {
    String email = request.getEmail();
    String token = request.getToken();
    User user = userRepository.findByEmail(email);

    if (user != null) {
      throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User newUser = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(encodedPassword)
        .build();

    userRepository.save(newUser);
    UserResponse response = UserResponse.fromEntity(newUser);
    redisTemplate.delete(token);

    return response;
  }

  /**
   * 이메일로 회원 정보 조회
   * 
   * @param String (String email)
   * @return User (UUID id, String name, String email, String provider,
   *         LocalDateTime createdAt)
   */
  @Override
  public User findByEmail(String email) {
    User user = userRepository.findByEmail(email);
    return user;
  }
}
