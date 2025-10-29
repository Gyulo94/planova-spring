package com.planova.server.workspace.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
import com.planova.server.project.response.ProjectResponse;
import com.planova.server.user.response.UserResponse;
import com.planova.server.workspace.entity.Workspace;

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
@Schema(title = "WORKSPACE_RES_01 : 워크스페이스 응답 DTO")
public class WorkspaceResponse {

  @Schema(description = "워크스페이스 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "워크스페이스 이름", example = "팀 플라노바")
  private String name;

  @Schema(description = "워크스페이스 이미지 URL", example = "https://example.com/image.png")
  private String image;

  @Schema(description = "워크스페이스 초대 코드", example = "ABCD1234")
  private String inviteCode;

  @Schema(description = "워크스페이스 소유자 정보", example = "사용자 정보 객체")
  private UserResponse owner;

  @Schema(description = "워크스페이스 생성일", example = "2023-10-05T14:48:00")
  private LocalDateTime createdAt;

  @Schema(description = "워크스페이스에 속한 프로젝트 목록", example = "프로젝트 정보 객체 리스트")
  private List<ProjectResponse> projects;

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
        .owner(UserResponse.fromEntity(workspace.getOwner(), imageService))
        .inviteCode(workspace.getInviteCode())
        .createdAt(workspace.getCreatedAt())
        .projects(workspace.getProjects().stream()
            .map(project -> ProjectResponse.fromEntity(project, imageService))
            .toList())
        .build();
  }
}
