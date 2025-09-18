package com.planova.server.workspaceMember.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.User;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspaceMember.entity.WorkspaceMember;
import com.planova.server.workspaceMember.entity.WorkspaceMemberId;
import com.planova.server.workspaceMember.entity.WorkspaceMemberRole;
import com.planova.server.workspaceMember.repository.WorkspaceMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

  private final WorkspaceMemberRepository workspaceMemberRepository;

  /**
   * 워크스페이스 멤버 생성
   * 
   * @param Workspace workspace, User owner
   */
  @Override
  public void createWorkspaceMember(Workspace workspace, User owner) {
    WorkspaceMember workspaceMember = WorkspaceMember.builder()
        .workspaceId(workspace.getId())
        .userId(owner.getId())
        .workspace(workspace)
        .user(owner)
        .role(WorkspaceMemberRole.ADMIN)
        .build();
    workspaceMemberRepository.save(workspaceMember);
  }

  /**
   * 워크스페이스 멤버 검증
   * 
   * @param Workspace workspace, User user
   * @throws ApiException (ErrorCode.MEMBER_NOT_FOUND)
   */
  @Override
  public void validateWorkspaceMember(Workspace workspace, User user) {
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspace.getId(), user.getId());
    boolean isMember = workspaceMemberRepository.existsById(workspaceMemberId);
    if (!isMember) {
      throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
    }
  }

  /**
   * 워크스페이스 소유자 검증
   * 
   * @param Workspace workspace, User user
   * @throws ApiException (ErrorCode.WORKSPACE_UNAUTHORIZED)
   */
  @Override
  public void validateWorkspaceOwner(Workspace workspace, User user) {
    if (workspace.getOwner().getId() != user.getId()) {
      throw new ApiException(ErrorCode.WORKSPACE_UNAUTHORIZED);
    }
  }

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param User user
   * @return List<Workspace>
   */
  @Override
  public List<Workspace> findWorkspaces(User user) {
    List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findAllByUser(user);
    List<Workspace> workspaces = workspaceMembers.stream()
        .map(WorkspaceMember::getWorkspace)
        .toList();
    return workspaces;
  }

  /**
   * 워크스페이스의 모든 멤버 삭제
   * 
   * @param Workspace workspace
   */
  @Override
  public void deleteWorkspaceMembers(Workspace workspace) {
    List<WorkspaceMember> members = workspaceMemberRepository.findAllByWorkspace(workspace);
    if (!members.isEmpty()) {
      workspaceMemberRepository.deleteAll(members);
    }
  }
}
