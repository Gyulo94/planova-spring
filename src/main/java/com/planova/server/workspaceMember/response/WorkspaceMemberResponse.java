package com.planova.server.workspaceMember.response;

import java.util.List;
import java.util.UUID;

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
public class WorkspaceMemberResponse {

  private UUID workspaceId;
  private List<WorkspaceMemberInfo> members;
  private int memberCount;

  public static WorkspaceMemberResponse fromEntity(UUID workspaceId, List<WorkspaceMemberInfo> members) {
    return WorkspaceMemberResponse.builder()
        .workspaceId(workspaceId)
        .members(members)
        .memberCount(members.size())
        .build();
  }
}
