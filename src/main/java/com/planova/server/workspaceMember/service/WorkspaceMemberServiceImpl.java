package com.planova.server.workspaceMember.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.User;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspaceMember.entity.WorkspaceMember;
import com.planova.server.workspaceMember.entity.WorkspaceMemberId;
import com.planova.server.workspaceMember.entity.WorkspaceMemberRole;
import com.planova.server.workspaceMember.repository.WorkspaceMemberRepository;
import com.planova.server.workspaceMember.request.WorkspaceMemberRequest;
import com.planova.server.workspaceMember.response.WorkspaceMemberInfoResponse;
import com.planova.server.workspaceMember.response.WorkspaceMemberResponse;

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
   * @param UUID workspaceId, UUID userId
   * @throws ApiException (ErrorCode.MEMBER_NOT_FOUND)
   */
  @Override
  public void validateWorkspaceMember(UUID workspaceId, UUID userId) {
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspaceId, userId);
    boolean isMember = workspaceMemberRepository.existsById(workspaceMemberId);
    if (!isMember) {
      throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
    }
  }

  /**
   * 워크스페이스 멤버가 ADMIN인지 검증
   * 
   * @param UUID workspaceId, UUID userId
   * @throws ApiException (ErrorCode.MEMBER_NOT_ADMIN, ErrorCode.MEMBER_NOT_FOUND)
   */
  public void validateWorkspaceAdmin(UUID workspaceId, UUID userId) {
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspaceId, userId);
    WorkspaceMember workspaceMember = workspaceMemberRepository.findById(workspaceMemberId)
        .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));
    if (workspaceMember.getRole() != WorkspaceMemberRole.ADMIN) {
      throw new ApiException(ErrorCode.MEMBER_NOT_ADMIN);
    }
  }

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param User user
   * @return List<Workspace>
   */
  @Override
  public List<Workspace> findWorkspaces(UUID userId) {
    List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findAllByUserId(userId);
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
  public void deleteWorkspaceMembers(UUID workspaceId) {
    List<WorkspaceMember> members = workspaceMemberRepository.findAllByWorkspaceId(workspaceId);
    if (!members.isEmpty()) {
      workspaceMemberRepository.deleteAll(members);
    }
  }

  /**
   * 워크스페이스 멤버 조회
   * 
   * @param UUID workspaceId, UUID userId
   * @return WorkspaceMemberResponse
   */
  @Override
  public WorkspaceMemberResponse findWorkspaceMembers(UUID workspaceId, UUID userId) {
    validateWorkspaceMember(workspaceId, userId);
    List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findAllByWorkspaceId(workspaceId);
    List<WorkspaceMemberInfoResponse> memberInfos = workspaceMembers.stream()
        .map(member -> WorkspaceMemberInfoResponse.fromEntity(member, member.getUser()))
        .toList();
    WorkspaceMemberResponse response = WorkspaceMemberResponse.fromEntity(workspaceId, memberInfos);
    return response;
  }

  /**
   * 워크스페이스내 나의 정보 조회
   * 
   * @param UUID workspaceId, UUID userId
   * @return WorkspaceMemberInfoResponse
   */
  @Override
  public WorkspaceMemberInfoResponse findMyWorkspaceMemberInfo(UUID workspaceId, UUID userId) {
    validateWorkspaceMember(workspaceId, userId);
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspaceId, userId);
    WorkspaceMember workspaceMember = workspaceMemberRepository.findById(workspaceMemberId)
        .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));
    User user = workspaceMember.getUser();
    WorkspaceMemberInfoResponse response = WorkspaceMemberInfoResponse.fromEntity(workspaceMember, user);
    return response;
  }

  /**
   * 워크스페이스 참가
   * 
   * @param UUID workspaceId, WorkspaceMemberRequest request, UUID userId
   * @return message - "워크스페이스에 성공적으로 참가하였습니다."
   */
  @Override
  public void joinWorkspace(UUID workspaceId, WorkspaceMemberRequest request, UUID userId) {
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspaceId, userId);
    boolean isMember = workspaceMemberRepository.existsById(workspaceMemberId);
    if (isMember) {
      throw new ApiException(ErrorCode.MEMBER_ALREADY_EXISTS);
    }
    workspaceMemberRepository.save(WorkspaceMemberRequest.toEntity(workspaceMemberId));
  }

  /**
   * 워크스페이스 멤버 권한 변경
   * 
   * @param UUID workspaceId, UUID memberId, UUID userId
   * @return message - "워크스페이스 멤버 권한이 성공적으로 변경되었습니다."
   */
  @Override
  public void updateWorkspaceMember(UUID workspaceId, UUID memberId, UUID userId) {
    validateWorkspaceMember(workspaceId, memberId);
    validateWorkspaceAdmin(workspaceId, userId);
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspaceId, memberId);
    WorkspaceMember workspaceMember = workspaceMemberRepository.findById(workspaceMemberId)
        .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

    if (workspaceMember.getRole() == WorkspaceMemberRole.ADMIN) {
      workspaceMember.updateRole(WorkspaceMemberRole.MEMBER);
    } else {
      workspaceMember.updateRole(WorkspaceMemberRole.ADMIN);
    }
    workspaceMemberRepository.save(workspaceMember);
  }

  /**
   * 워크스페이스 멤버 추방
   * 
   * @param UUID workspaceId, UUID memberId, UUID userId
   * @return message - "워크스페이스 멤버가 성공적으로 추방되었습니다."
   */
  @Override
  public void removeWorkspaceMember(UUID workspaceId, UUID memberId, UUID userId) {
    validateWorkspaceMember(workspaceId, memberId);
    validateWorkspaceAdmin(workspaceId, userId);
    WorkspaceMemberId workspaceMemberId = new WorkspaceMemberId(workspaceId, memberId);
    workspaceMemberRepository.deleteById(workspaceMemberId);
  }
}
