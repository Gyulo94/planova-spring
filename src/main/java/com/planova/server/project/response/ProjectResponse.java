package com.planova.server.project.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.entity.Image;
import com.planova.server.image.service.ImageService;
import com.planova.server.project.entity.Project;

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
public class ProjectResponse {
  private UUID id;
  private String name;
  private String image;
  private UUID workspaceId;
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
