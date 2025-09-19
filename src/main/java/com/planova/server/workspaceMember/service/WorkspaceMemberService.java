package com.planova.server.workspaceMember.service;

import java.util.List;
import java.util.UUID;

import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.User;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspaceMember.request.WorkspaceMemberRequest;
import com.planova.server.workspaceMember.response.WorkspaceMemberResponse;

public interface WorkspaceMemberService {

  /**
   * 워크스페이스 멤버 생성
   * 
   * @param Workspace workspace, User owner
   */
  public void createWorkspaceMember(Workspace workspace, User owner);

  /**
   * 워크스페이스 멤버 검증
   * 
   * @param Workspace workspace, User user
   * @throws ApiException (ErrorCode.MEMBER_NOT_FOUND)
   */
  public void validateWorkspaceMember(UUID workspaceId, UUID userId);

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param User user
   * @return List<Workspace>
   */
  public List<Workspace> findWorkspaces(UUID userId);

  /**
   * 워크스페이스의 모든 멤버 삭제
   * 
   * @param Workspace workspace
   */
  public void deleteWorkspaceMembers(UUID workspaceId);

  /**
   * 워크스페이스 참가
   * 
   * @param UUID workspaceId, WorkspaceMemberRequest request, UUID userId
   */
  public void joinWorkspace(UUID workspaceId, WorkspaceMemberRequest request, UUID userId);

  /**
   * 워크스페이스 멤버 조회
   * 
   * @param Workspace workspace, User user
   * @return WorkspaceMemberResponse
   */
  public WorkspaceMemberResponse findWorkspaceMembers(UUID workspaceId, UUID userId);
}
