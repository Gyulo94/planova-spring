package com.planova.server.workspace.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planova.server.global.constants.Constants;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.global.util.GenerateInviteCodeUtils;
import com.planova.server.image.entity.EntityType;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.entity.User;
import com.planova.server.user.service.UserService;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspace.repository.WorkspaceRepository;
import com.planova.server.workspace.request.WorkspaceRequest;
import com.planova.server.workspace.response.WorkspaceResponse;
import com.planova.server.workspaceMember.service.WorkspaceMemberService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

  private final WorkspaceRepository workspaceRepository;
  private final ImageService imageService;
  private final UserService userService;
  private final Constants constants;
  private final WorkspaceMemberService workspaceMemberService;

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param UUID userId
   * @return List<WorkspaceResponse> (UUID id, String name, String image)
   */
  @Transactional
  @Override
  public List<WorkspaceResponse> findWorkspaces(UUID userId) {
    List<Workspace> workspaces = workspaceMemberService.findWorkspaces(userId);
    List<WorkspaceResponse> response = workspaces.stream()
        .map(workspace -> WorkspaceResponse.fromEntity(workspace, imageService))
        .toList();
    return response;
  }

  /**
   * 워크스페이스 상세 조회
   * 
   * @param UUID id, UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  @Override
  public WorkspaceResponse findWorkspaceById(UUID id, UUID userId) {
    Workspace workspace = findWorkspaceEntityById(id);
    WorkspaceResponse response = WorkspaceResponse.fromEntity(workspace, imageService);
    return response;
  }

  /**
   * 워크스페이스 엔터티 반환 메서드 (공개)
   */
  public Workspace getWorkspaceEntityById(UUID id) {
    return findWorkspaceEntityById(id);
  }

  /**
   * 워크스페이스 엔터티 반환 메서드 (비공개 내수용)
   */
  private Workspace findWorkspaceEntityById(UUID id) {
    Workspace workspace = workspaceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.WORKSPACE_NOT_FOUND));
    return workspace;
  }

  /**
   * 워크스페이스 생성
   * 
   * @param WorkspaceRequest (String name, String image), UUID userId
   * @return WorkspaceResponse (UUID id, String name, String image)
   */
  @Transactional
  @Override
  public WorkspaceResponse createWorkspace(WorkspaceRequest request, UUID userId) {
    User user = userService.getUserEntityById(userId);
    String inviteCode = GenerateInviteCodeUtils.generateInviteCode(8);
    Workspace newWorkspace = workspaceRepository.save(WorkspaceRequest.toEntity(request, inviteCode, user));
    String newImage = imageService.createImages(newWorkspace.getId(), List.of(request.getImage()), EntityType.WORKSPACE)
        .get(0);
    workspaceMemberService.createWorkspaceMember(newWorkspace, user);
    WorkspaceResponse response = WorkspaceResponse.fromEntity(newWorkspace, newImage);
    return response;
  }

  /**
   * 워크스페이스 수정
   * 
   * @param UUID id, WorkspaceRequest (String name, String image), UUID userId
   * @return WorkspaceResponse (UUID id, String name, String image)
   */
  @Transactional
  @Override
  public WorkspaceResponse updateWorkspace(UUID id, WorkspaceRequest request, UUID userId) {
    Workspace workspace = findWorkspaceEntityById(id);
    validateWorkspaceOwner(workspace.getOwner().getId(), userId);
    workspace.update(request.getName());
    String existingImage = imageService.findImagesByEntityId(workspace.getId(), EntityType.WORKSPACE).get(0).getUrl();
    String newImage = imageService
        .updateImages(workspace.getId(), List.of(request.getImage()), List.of(existingImage), EntityType.WORKSPACE)
        .get(0);
    WorkspaceResponse response = WorkspaceResponse.fromEntity(workspace, newImage);
    return response;
  }

  /**
   * 워크스페이스 삭제
   * 
   * @param UUID id, UUID userId
   * @return String (삭제 성공 메시지)
   */
  @Transactional
  @Override
  public void deleteWorkspace(UUID id, UUID userId) {
    Workspace workspace = findWorkspaceEntityById(id);
    validateWorkspaceOwner(workspace.getOwner().getId(), userId);
    int ownerWorkspaceCount = workspaceRepository.countByOwnerId(userId);
    if (ownerWorkspaceCount == 1) {
      throw new ApiException(ErrorCode.WORKSPACE_HAS_TO_EXIST_AT_LEAST_ONE);
    }
    imageService.deleteImages(workspace.getId(), constants.getProjectName(), EntityType.WORKSPACE);
    workspaceMemberService.deleteWorkspaceMembers(id);
    workspaceRepository.delete(workspace);
  }

  /**
   * 워크스페이스 초대 코드 재발급
   * 
   * @param UUID id, UUID userId
   */
  @Transactional
  @Override
  public void resetInviteCode(UUID id, UUID userId) {
    Workspace workspace = findWorkspaceEntityById(id);
    validateWorkspaceOwner(workspace.getOwner().getId(), userId);
    String newInviteCode = GenerateInviteCodeUtils.generateInviteCode(8);
    workspace.updateInviteCode(newInviteCode);
  }

  /**
   * 워크스페이스 소유자 검증
   * 
   * @param Workspace workspace, User user
   * @throws ApiException (ErrorCode.WORKSPACE_UNAUTHORIZED)
   */
  @Override
  public void validateWorkspaceOwner(UUID ownerId, UUID userId) {
    if (!ownerId.equals(userId)) {
      throw new ApiException(ErrorCode.WORKSPACE_UNAUTHORIZED);
    }
  }
}
