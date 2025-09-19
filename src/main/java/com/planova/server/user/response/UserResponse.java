package com.planova.server.user.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.user.entity.Provider;
import com.planova.server.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponse {

  private UUID id;
  private String name;
  private String email;
  private String image;
  private Provider provider;
  private LocalDateTime createdAt;

  public static UserResponse fromEntity(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .image(user.getImage())
        .provider(user.getProvider())
        .createdAt(user.getCreatedAt())
        .build();
  }
}