package com.planova.server.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
  private long expiresIn;

  public static TokenResponse from(String accessToken, String refreshToken, long expiresIn) {
    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .expiresIn(expiresIn)
        .build();
  }
}
