package com.planova.server.auth.response;

import com.planova.server.user.response.UserResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Schema(title = "AUTH_RES_01 : 로그인 응답 DTO")
public class LoginResponse {

  @Schema(description = "사용자 정보")
  private UserResponse user;

  @Schema(description = "사용자 토큰 정보")
  private TokenResponse serverTokens;

  public LoginResponse from(UserResponse user, TokenResponse serverTokens) {
    return LoginResponse.builder()
        .user(user)
        .serverTokens(serverTokens)
        .build();
  }
}
