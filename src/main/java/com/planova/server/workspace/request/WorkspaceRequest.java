package com.planova.server.workspace.request;

import com.planova.server.user.entity.User;
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
public class WorkspaceRequest {

  @NotBlank(message = "워크스페이스 이름은 필수 입력값입니다.")
  private String name;

  private String image;

  public static Workspace toEntity(WorkspaceRequest request, String inviteCode, User owner) {
    return Workspace.builder()
        .name(request.getName())
        .inviteCode(inviteCode)
        .owner(owner)
        .build();
  }
}
