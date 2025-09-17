package com.planova.server.workspace.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
    User user = userService.getUserEntityById(userId);
    List<Workspace> workspaces = workspaceMemberService.findWorkspaces(user);
    List<WorkspaceResponse> response = workspaces.stream()
        .map(workspace -> WorkspaceResponse.fromEntity(workspace, imageService))
        .toList();
    return response;
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

}
