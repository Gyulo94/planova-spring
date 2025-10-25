package com.planova.server.user.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
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

  public static UserResponse fromEntity(User user, ImageService imageService) {
    String imageUrl = imageService.findImagesByEntityId(user.getId(), EntityType.USER)
        .stream()
        .findFirst()
        .map(Image::getUrl)
        .orElse(null);
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .image(imageUrl)
        .provider(user.getProvider())
        .createdAt(user.getCreatedAt())
        .build();
  }
}