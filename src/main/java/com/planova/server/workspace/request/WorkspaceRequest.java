package com.planova.server.workspace.request;

import com.planova.server.user.entity.User;
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
@Schema(title = "WORKSPACE_REQ_01 : 워크스페이스 생성 요청 DTO")
public class WorkspaceRequest {

  @NotBlank(message = "워크스페이스 이름은 필수 입력값입니다.")
  @Schema(description = "워크스페이스 이름", example = "팀 플라노바")
  private String name;

  @Schema(description = "워크스페이스 이미지", example = "https://example.com/image.png")
  private String image;

  public static Workspace toEntity(WorkspaceRequest request, String inviteCode, User owner) {
    return Workspace.builder()
        .name(request.getName())
        .inviteCode(inviteCode)
        .owner(owner)
        .build();
  }
}
