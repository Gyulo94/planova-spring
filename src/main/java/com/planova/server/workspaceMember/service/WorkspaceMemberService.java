package com.planova.server.workspaceMember.service;

import java.util.List;

import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.User;
import com.planova.server.workspace.entity.Workspace;

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
  public void validateWorkspaceMember(Workspace workspace, User user);

  /**
   * 워크스페이스 소유자 검증
   * 
   * @param Workspace workspace, User user
   * @throws ApiException (ErrorCode.WORKSPACE_UNAUTHORIZED)
   */
  public void validateWorkspaceOwner(Workspace workspace, User user);

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param User user
   * @return List<Workspace>
   */
  public List<Workspace> findWorkspaces(User user);

  /**
   * 워크스페이스의 모든 멤버 삭제
   * 
   * @param Workspace workspace
   */
  public void deleteWorkspaceMembers(Workspace workspace);
}
