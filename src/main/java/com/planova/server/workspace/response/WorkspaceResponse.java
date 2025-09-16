package com.planova.server.workspace.response;

import java.util.UUID;

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

  public static WorkspaceResponse fromEntity(Workspace workspace, String image) {
    return WorkspaceResponse.builder()
        .id(workspace.getId())
        .name(workspace.getName())
        .image(image)
        .build();
  }
}
