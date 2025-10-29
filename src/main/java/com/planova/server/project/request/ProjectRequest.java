package com.planova.server.project.request;

import java.util.UUID;

import com.planova.server.project.entity.Project;
import com.planova.server.workspace.entity.Workspace;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "PROJECT_REQ_01 : 프로젝트 생성 요청 DTO")
public class ProjectRequest {

  @NotBlank(message = "프로젝트 이름은 필수 입력값입니다.")
  @Schema(description = "프로젝트 이름", example = "플라노바 개발")
  private String name;

  @Schema(description = "프로젝트 이미지", example = "https://example.com/project-image.png")
  private String image;

  @Schema(description = "워크스페이스 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID workspaceId;

  public static Project toEntity(ProjectRequest request, Workspace workspace) {
    return Project.builder()
        .name(request.getName())
        .workspace(workspace)
        .build();
  }
}
