package com.planova.server.user.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.entity.Provider;
import com.planova.server.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "USER_RES_01 : 사용자 응답 DTO")
public class UserResponse {

  @Schema(description = "사용자 고유 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "사용자 이름", example = "John Doe")
  private String name;

  @Schema(description = "사용자 이메일", example = "john.doe@example.com")
  private String email;

  @Schema(description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
  private String image;

  @Schema(description = "계정 유형", example = "이메일 || 구글 || 카카오")
  private Provider provider;

  @Schema(description = "계정 생성 일시", example = "2023-10-05T14:48:00")
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