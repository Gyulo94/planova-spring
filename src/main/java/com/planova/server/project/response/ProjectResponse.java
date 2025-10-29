package com.planova.server.project.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
import com.planova.server.project.entity.Project;

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
@Schema(title = "PROJECT_RES_01 : 프로젝트 응답 DTO")
public class ProjectResponse {

  @Schema(description = "프로젝트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "프로젝트 이름", example = "플라노바 개발")
  private String name;

  @Schema(description = "프로젝트 이미지 URL", example = "https://example.com/project-image.png")
  private String image;

  @Schema(description = "워크스페이스 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID workspaceId;

  @Schema(description = "프로젝트 생성일", example = "2023-10-05T14:48:00")
  private LocalDateTime createdAt;

  public static ProjectResponse fromEntity(Project project, String image) {
    return ProjectResponse.builder()
        .id(project.getId())
        .name(project.getName())
        .image(image)
        .workspaceId(project.getWorkspace().getId())
        .createdAt(project.getCreatedAt())
        .build();
  }

  public static ProjectResponse fromEntity(Project project, ImageService imageService) {
    String imageUrl = imageService.findImagesByEntityId(project.getId(), EntityType.PROJECT)
        .stream()
        .findFirst()
        .map(Image::getUrl)
        .orElse(null);

    return ProjectResponse.builder()
        .id(project.getId())
        .name(project.getName())
        .image(imageUrl)
        .workspaceId(project.getWorkspace().getId())
        .createdAt(project.getCreatedAt())
        .build();
  }
}
