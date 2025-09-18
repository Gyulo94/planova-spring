package com.planova.server.workspace.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.response.UserResponse;
import com.planova.server.workspace.entity.Workspace;

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
public class WorkspaceResponse {
  private UUID id;
  private String name;
  private String image;
  private String inviteCode;
  private UserResponse owner;
  private LocalDateTime createdAt;

  public static WorkspaceResponse fromEntity(Workspace workspace, String image) {
    return WorkspaceResponse.builder()
        .id(workspace.getId())
        .name(workspace.getName())
        .image(image)
        .build();
  }

  public static WorkspaceResponse fromEntity(Workspace workspace, ImageService imageService) {
    String imageUrl = imageService.findImagesByEntityId(workspace.getId(), EntityType.WORKSPACE)
        .stream()
        .findFirst()
        .map(Image::getUrl)
        .orElse(null);

    return WorkspaceResponse.builder()
        .id(workspace.getId())
        .name(workspace.getName())
        .image(imageUrl)
        .owner(UserResponse.fromEntity(workspace.getOwner()))
        .inviteCode(workspace.getInviteCode())
        .createdAt(workspace.getCreatedAt())
        .build();
  }
}
