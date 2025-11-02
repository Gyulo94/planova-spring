package com.planova.server.project.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planova.server.global.constants.Constants;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.image.entity.EntityType;
import com.planova.server.image.service.ImageService;
import com.planova.server.project.entity.Project;
import com.planova.server.project.repository.ProjectRepository;
import com.planova.server.project.request.ProjectRequest;
import com.planova.server.project.response.ProjectResponse;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspace.service.WorkspaceService;
import com.planova.server.workspaceMember.service.WorkspaceMemberService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final WorkspaceMemberService workspaceMemberService;
  private final ProjectRepository projectRepository;
  private final ImageService imageService;
  private final Constants constants;
  private final WorkspaceService workspaceService;

  @Override
  public List<ProjectResponse> findProjectsByWorkspaceId(UUID workspaceId, UUID userId) {
    workspaceMemberService.validateWorkspaceMember(workspaceId, userId);
    List<Project> projects = projectRepository.findAllByWorkspaceId(workspaceId);
    List<ProjectResponse> response = projects.stream()
        .map(project -> ProjectResponse.fromEntity(project, imageService))
        .toList();
    return response;
  }

  @Transactional
  @Override
  public ProjectResponse createProject(ProjectRequest request, UUID userId) {
    Workspace workspace = workspaceService.getWorkspaceEntityById(request.getWorkspaceId());
    workspaceMemberService.validateWorkspaceAdmin(request.getWorkspaceId(), userId);
    Project newProject = projectRepository.save(ProjectRequest.toEntity(request, workspace));
    String newImage = "";
    if (!request.getImage().isEmpty()) {
      newImage = imageService.createImages(newProject.getId(), List.of(request.getImage()), EntityType.PROJECT)
          .get(0);
    }
    ProjectResponse response = ProjectResponse.fromEntity(newProject, newImage);
    return response;
  }

  public Project getProjectEntityById(UUID id) {
    return findProjectEntityById(id);
  }

  private Project findProjectEntityById(UUID id) {
    Project response = projectRepository.findById(id).orElseThrow(
        () -> new ApiException(ErrorCode.PROJECT_NOT_FOUND));
    return response;
  }

  @Override
  public ProjectResponse findProjectById(UUID id, UUID userId) {
    Project project = getProjectEntityById(id);
    workspaceMemberService.validateWorkspaceMember(project.getWorkspace().getId(), userId);
    ProjectResponse response = ProjectResponse.fromEntity(project, imageService);
    return response;
  }

  @Transactional
  @Override
  public ProjectResponse updateProject(UUID id, ProjectRequest request, UUID userId) {
    Project project = findProjectEntityById(id);
    Workspace workspace = project.getWorkspace();
    workspaceService.validateWorkspaceOwner(workspace.getOwner().getId(), userId);
    project.update(request.getName());
    String newImage = "";

    var existingImages = imageService.findImagesByEntityId(project.getId(), EntityType.PROJECT);
    String existingImage = (existingImages != null && !existingImages.isEmpty())
        ? existingImages.get(0).getUrl()
        : null;

    if (request.getImage() != null && !request.getImage().isEmpty()) {
      if (existingImage != null) {
        newImage = imageService
            .updateImages(project.getId(), List.of(request.getImage()), List.of(existingImage), EntityType.PROJECT)
            .get(0);
      } else {
        newImage = imageService.createImages(project.getId(), List.of(request.getImage()), EntityType.PROJECT)
            .get(0);
      }
    } else {
      newImage = existingImage != null ? existingImage : "";
    }
    ProjectResponse response = ProjectResponse.fromEntity(project, newImage);
    return response;
  }

  @Override
  public void deleteProject(UUID id, UUID userId) {
    Project project = findProjectEntityById(id);
    Workspace workspace = project.getWorkspace();
    workspaceService.validateWorkspaceOwner(workspace.getOwner().getId(), userId);
    imageService.deleteImages(project.getId(), constants.getProjectName(), EntityType.PROJECT);
    workspaceMemberService.deleteWorkspaceMembers(id);
    projectRepository.delete(project);
  }

}
