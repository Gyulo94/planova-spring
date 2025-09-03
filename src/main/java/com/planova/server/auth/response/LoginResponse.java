package com.planova.server.auth.response;

import com.planova.server.user.response.UserResponse;

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
public class LoginResponse {
  private UserResponse user;
  private TokenResponse serverTokens;

  public LoginResponse from(UserResponse user, TokenResponse serverTokens) {
    return LoginResponse.builder()
        .user(user)
        .serverTokens(serverTokens)
        .build();
  }
}
