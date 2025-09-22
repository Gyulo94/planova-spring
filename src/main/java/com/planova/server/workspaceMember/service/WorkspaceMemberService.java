package com.planova.server.workspaceMember.service;

import java.util.List;
import java.util.UUID;

import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.User;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspaceMember.request.WorkspaceMemberRequest;
import com.planova.server.workspaceMember.response.WorkspaceMemberInfoResponse;
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
   * 워크스페이스 멤버가 ADMIN인지 검증
   * 
   * @param UUID workspaceId, UUID userId
   * @throws ApiException (ErrorCode.MEMBER_NOT_ADMIN, ErrorCode.MEMBER_NOT_FOUND)
   */
  public void validateWorkspaceAdmin(UUID workspaceId, UUID userId);

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
   * 워크스페이스 멤버 조회
   * 
   * @param UUID workspaceId, UUID userId
   * @return WorkspaceMemberResponse
   */
  public WorkspaceMemberResponse findWorkspaceMembers(UUID workspaceId, UUID userId);

  /**
   * 워크스페이스내 나의 정보 조회
   * 
   * @param UUID workspaceId, UUID userId
   * @return WorkspaceMemberInfoResponse
   */
  public WorkspaceMemberInfoResponse findMyWorkspaceMemberInfo(UUID workspaceId, UUID userId);

  /**
   * 워크스페이스 참가
   * 
   * @param UUID workspaceId, WorkspaceMemberRequest request, UUID userId
   * @return message - "워크스페이스에 성공적으로 참가하였습니다."
   */
  public void joinWorkspace(UUID workspaceId, WorkspaceMemberRequest request, UUID userId);

  /**
   * 워크스페이스 멤버 권한 변경
   * 
   * @param UUID workspaceId, UUID memberId, UUID userId
   * @return message - "워크스페이스 멤버 권한이 성공적으로 변경되었습니다."
   */
  public void updateWorkspaceMember(UUID workspaceId, UUID memberId, UUID userId);

  /**
   * 워크스페이스 멤버 추방
   * 
   * @param UUID workspaceId, UUID memberId, UUID userId
   * @return message - "워크스페이스 멤버가 성공적으로 추방되었습니다."
   */
  public void removeWorkspaceMember(UUID workspaceId, UUID memberId, UUID userId);
}
