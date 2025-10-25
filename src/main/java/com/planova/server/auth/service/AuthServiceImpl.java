package com.planova.server.auth.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.planova.server.auth.request.LoginRequest;
import com.planova.server.auth.request.SocialLoginRequest;
import com.planova.server.auth.response.LoginResponse;
import com.planova.server.auth.response.TokenResponse;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.global.jwt.JwtProvider;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.entity.User;
import com.planova.server.user.response.UserResponse;
import com.planova.server.user.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final ImageService imageService;

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
  @Override
  @Transactional
  public LoginResponse login(LoginRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();
    User user = userService.findByEmail(email);

    if (user == null) {
      throw new ApiException(ErrorCode.FAILED_TO_LOGIN);
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new ApiException(ErrorCode.FAILED_TO_LOGIN);
    }
    TokenResponse newTokens = generateTokens(user);
    LoginResponse response = LoginResponse.builder()
        .user(UserResponse.fromEntity(user, imageService))
        .serverTokens(newTokens)
        .build();
    return response;
  }

  /**
   * 소셜 로그인 (카카오, 구글)
   * 
   * @param SocialLoginRequest (String email, String password, String name, String
   *                           image, String provider)
   * @return LoginResponse (user: UserResponse, serverTokens: TokenResponse)
   *         - user: UserResponse (UUID id, String name, String email, String
   *         image, String provider, LocalDateTime createdAt)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  public LoginResponse socialLogin(SocialLoginRequest request) {
    String email = request.getEmail();
    User user = userService.findByEmail(email);

    if (user == null) {
      userService.socialSignup(request);
    }
    user = userService.findByEmail(email);
    TokenResponse newTokens = generateTokens(user);
    LoginResponse response = LoginResponse.builder()
        .user(UserResponse.fromEntity(user, imageService))
        .serverTokens(newTokens)
        .build();

    LOGGER.info("소셜 로그인 완료: {}", response);
    return response;
  };

  /**
   * 리프레시 토큰으로 액세스 토큰 재발급
   * 
   * @param authorization Authorization 헤더에 포함된 리프레시 토큰
   * @return TokenResponse (serverTokens: TokenResponse)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  @Override
  @Transactional
  public TokenResponse refresh(String authorization) {
    String oldRefreshToken = parserRefreshToken(authorization);
    if (oldRefreshToken == null) {
      throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
    Claims claims = jwtProvider.validateRefreshToken(oldRefreshToken);
    if (claims == null) {
      throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    String userIdStr = claims.getSubject();
    if (userIdStr == null) {
      LOGGER.warn("Refresh Token Claims에 사용자 ID가 없습니다.");
      throw new ApiException(ErrorCode.REFRESH_TOKEN_CLAIMS_NOT_FOUND);
    }
    UUID userId;
    try {
      userId = UUID.fromString(userIdStr);
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Refresh Token Claims의 사용자 ID 형식이 올바르지 않습니다. (UUID 형식 아님): {}", userIdStr, e);
      throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
    User user = userService.getUserEntityById(userId);
    TokenResponse newTokens = generateTokens(user);
    LOGGER.info("Refresh Token 재발급 완료");
    return newTokens;
  }

  /**
   * 새로운 토큰 생성
   * 
   * @param User (UUID id, String email String name String password String image
   *             Provider provider LocalDateTime createdAt)
   * @return TokenResponse (serverTokens: TokenResponse)
   *         - serverTokens: TokenResponse (String accessToken, String
   *         refreshToken, long expiresIn)
   */
  private TokenResponse generateTokens(User user) {
    String accessToken = jwtProvider.createAccessToken(user.getId());
    String refreshToken = jwtProvider.createRefreshToken(user.getId());
    long expiresIn = new Date().getTime() + jwtProvider.accessTokenExpiredAt * 1000;

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .expiresIn(expiresIn)
        .build();
  }

  private String parserRefreshToken(String authorization) {
    final String REFRESH_PREFIX = "Refresh ";
    final int PREFIX_LENGTH = REFRESH_PREFIX.length();
    if (StringUtils.hasText(authorization) && authorization.startsWith(REFRESH_PREFIX)) {
      if (authorization.length() > PREFIX_LENGTH) {
        return authorization.substring(PREFIX_LENGTH);
      }
    }
    return null;
  }

}
