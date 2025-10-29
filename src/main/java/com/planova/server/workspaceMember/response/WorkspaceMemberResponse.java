package com.planova.server.workspaceMember.response;

import java.util.List;
import java.util.UUID;

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
@Schema(title = "WORKSPACE_MEMBER_RES_01 : 워크스페이스 멤버 응답 DTO")
public class WorkspaceMemberResponse {

  @Schema(description = "워크스페이스 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID workspaceId;

  @Schema(description = "워크스페이스 멤버 리스트")
  private List<WorkspaceMemberInfoResponse> members;

  @Schema(description = "워크스페이스 멤버 수", example = "5")
  private int memberCount;

  public static WorkspaceMemberResponse fromEntity(UUID workspaceId, List<WorkspaceMemberInfoResponse> members) {
    return WorkspaceMemberResponse.builder()
        .workspaceId(workspaceId)
        .members(members)
        .memberCount(members.size())
        .build();
  }
}
