package com.planova.server.workspaceMember.request;

import com.planova.server.workspaceMember.entity.WorkspaceMember;
import com.planova.server.workspaceMember.entity.WorkspaceMemberId;
import com.planova.server.workspaceMember.entity.WorkspaceMemberRole;

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
@Schema(title = "WORKSPACE_MEMBER_REQ_01 : 워크스페이스 멤버 추가 요청 DTO")
public class WorkspaceMemberRequest {

  @NotBlank(message = "워크스페이스 초대 코드는 필수 입력값입니다.")
  @Schema(description = "워크스페이스 초대 코드", example = "INVITE1234")
  private String inviteCode;

  public static WorkspaceMember toEntity(WorkspaceMemberId workspaceMemberId) {
    return WorkspaceMember.builder()
        .workspaceId(workspaceMemberId.getWorkspaceId())
        .userId(workspaceMemberId.getUserId())
        .role(WorkspaceMemberRole.MEMBER)
        .build();
  }
}
