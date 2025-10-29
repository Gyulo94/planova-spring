package com.planova.server.workspaceMember.response;

import java.time.LocalDateTime;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.entity.User;
import com.planova.server.user.response.UserResponse;
import com.planova.server.workspaceMember.entity.WorkspaceMember;
import com.planova.server.workspaceMember.entity.WorkspaceMemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(title = "WORKSPACE_MEMBER_RES_02 : 워크스페이스 멤버 정보 응답 DTO")
public class WorkspaceMemberInfoResponse extends UserResponse {

  @Schema(description = "워크스페이스 멤버 역할", example = "ADMIN || MEMBER")
  private WorkspaceMemberRole role;

  @Schema(description = "워크스페이스 가입 일시", example = "2023-10-05T14:48:00")
  private LocalDateTime joinedAt;

  public static WorkspaceMemberInfoResponse fromEntity(WorkspaceMember workspaceMember, User user,
      ImageService imageService) {
    String imageUrl = imageService.findImagesByEntityId(user.getId(), EntityType.USER)
        .stream()
        .findFirst()
        .map(Image::getUrl)
        .orElse(null);
    return WorkspaceMemberInfoResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .image(imageUrl)
        .provider(user.getProvider())
        .createdAt(user.getCreatedAt())
        .role(workspaceMember.getRole())
        .joinedAt(workspaceMember.getCreatedAt())
        .build();
  }

}