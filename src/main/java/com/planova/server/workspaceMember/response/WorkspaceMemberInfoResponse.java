package com.planova.server.workspaceMember.response;

import java.time.LocalDateTime;

import com.planova.server.user.entity.User;
import com.planova.server.user.response.UserResponse;
import com.planova.server.workspaceMember.entity.WorkspaceMember;
import com.planova.server.workspaceMember.entity.WorkspaceMemberRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkspaceMemberInfoResponse extends UserResponse {

  private WorkspaceMemberRole role;
  private LocalDateTime joinedAt;

  public static WorkspaceMemberInfoResponse fromEntity(WorkspaceMember workspaceMember, User user) {
    return WorkspaceMemberInfoResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .image(user.getImage())
        .provider(user.getProvider())
        .createdAt(user.getCreatedAt())
        .role(workspaceMember.getRole())
        .joinedAt(workspaceMember.getCreatedAt())
        .build();
  }

}