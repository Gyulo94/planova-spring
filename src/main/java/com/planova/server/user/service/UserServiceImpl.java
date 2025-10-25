package com.planova.server.user.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planova.server.auth.request.SocialLoginRequest;
import com.planova.server.global.constants.Constants;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.image.entity.EntityType;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.entity.User;
import com.planova.server.user.repository.UserRepository;
import com.planova.server.user.request.ResetPasswordRequest;
import com.planova.server.user.request.UserPasswordRequest;
import com.planova.server.user.request.UserRequest;
import com.planova.server.user.request.UserUpdateRequest;
import com.planova.server.user.response.UserResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, String> redisTemplate;
  private final ImageService imageService;
  private final Constants constants;

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

    UserResponse response = UserResponse.fromEntity(newUser, imageService);
    redisTemplate.delete(token);

    return response;
  }

  /**
   * 비밀번호 재설정
   * 
   * @param ResetPasswordRequest (String email, String token, String password)
   * @return String (String message)
   *         - message: "비밀번호가 성공적으로 변경되었습니다. 다시 로그인을 해주세요."
   */
  @Transactional
  @Override
  public String resetPassword(ResetPasswordRequest request) {
    String email = request.getEmail();
    String token = request.getToken();
    String newPassword = request.getPassword();
    User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new ApiException(ErrorCode.USER_NOT_FOUND);
    }

    if (passwordEncoder.matches(newPassword, user.getPassword())) {
      throw new ApiException(ErrorCode.SAME_PASSWORD);
    }

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.updatePassword(encodedPassword);
    redisTemplate.delete(token);

    // LOGGER.info("비밀번호 재설정 완료. Email: {}", email);
    return null;
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

  /**
   * 유저 엔터티 반환 메서드 (공개)
   */
  public User getUserEntityById(UUID id) {
    return findUserEntityById(id);
  }

  /**
   * 유저 엔터티 반환 메서드 (비공개 내수용)
   */
  private User findUserEntityById(UUID id) {
    User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    return user;
  }

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
  public void socialSignup(SocialLoginRequest request) {
    String email = request.getEmail();
    User user = userRepository.findByEmail(email);

    if (user != null) {
      throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
    }

    User newUser = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .provider(request.getProvider())
        .build();
    userRepository.save(newUser);
    imageService.createUserImage(newUser.getId(), request.getImage(), EntityType.USER);
  }

  @Override
  public UserResponse findUserById(UUID id) {
    User user = findUserEntityById(id);
    UserResponse response = UserResponse.fromEntity(user, imageService);
    return response;
  }

  @Override
  public UserResponse updateUser(UUID id, UserUpdateRequest request) {
    User user = findUserEntityById(id);
    user.update(request.getName());

    var existingImages = imageService.findImagesByEntityId(id, EntityType.USER);

    String existingImage = (existingImages != null && !existingImages.isEmpty())
        ? existingImages.get(0).getUrl()
        : null;

    if (request.getImage() != null && !request.getImage().isEmpty()) {
      if (existingImage != null) {
        imageService
            .updateImages(id, List.of(request.getImage()), List.of(existingImage), EntityType.USER)
            .get(0);
      } else {
        imageService.createImages(id, List.of(request.getImage()), EntityType.USER)
            .get(0);
      }
    }
    UserResponse response = UserResponse.fromEntity(user, imageService);
    return response;
  }

  @Override
  public UserResponse updateUserPassword(UUID id, UserPasswordRequest request) {
    User user = findUserEntityById(id);

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new ApiException(ErrorCode.INVALID_CURRENT_PASSWORD);
    }

    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
      throw new ApiException(ErrorCode.SAME_PASSWORD);
    }

    String encodedPassword = passwordEncoder.encode(request.getNewPassword());
    user.updatePassword(encodedPassword);

    UserResponse response = UserResponse.fromEntity(user, imageService);
    return response;
  }

  @Override
  public void deleteUser(UUID id) {
    User user = findUserEntityById(id);
    imageService.deleteImages(id, constants.getProjectName(), EntityType.USER);
    userRepository.delete(user);
  }
}
