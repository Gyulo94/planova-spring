package com.planova.server.workspace.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planova.server.image.entity.EntityType;
import com.planova.server.image.service.ImageService;
import com.planova.server.user.entity.User;
import com.planova.server.user.service.UserService;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspace.repository.WorkspaceRepository;
import com.planova.server.workspace.request.WorkspaceRequest;
import com.planova.server.workspace.response.WorkspaceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

  private final WorkspaceRepository workspaceRepository;
  private final ImageService imageService;
  private final UserService userService;

  @Override
  public List<WorkspaceResponse> findWorkspaces(UUID userId) {
    User user = userService.getUserEntityById(userId);
    List<Workspace> workspaces = workspaceRepository.findAllByUser(user);
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
  @Override
  public WorkspaceResponse createWorkspace(WorkspaceRequest request, UUID userId) {
    User user = userService.getUserEntityById(userId);
    Workspace newWorkspace = workspaceRepository.save(WorkspaceRequest.toEntity(request, user));
    String newImage = imageService.createImages(newWorkspace.getId(), List.of(request.getImage()), EntityType.WORKSPACE)
        .get(0);
    WorkspaceResponse response = WorkspaceResponse.fromEntity(newWorkspace, newImage);
    return response;
  }

}
