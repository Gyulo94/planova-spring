package com.planova.server.project.request;

import java.util.UUID;

import com.planova.server.project.entity.Project;
import com.planova.server.workspace.entity.Workspace;

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
public class ProjectRequest {
  @NotBlank(message = "프로젝트 이름은 필수 입력값입니다.")
  private String name;

  private String image;
  private UUID workspaceId;

  public static Project toEntity(ProjectRequest request, Workspace workspace) {
    return Project.builder()
        .name(request.getName())
        .workspace(workspace)
        .build();
  }
}
