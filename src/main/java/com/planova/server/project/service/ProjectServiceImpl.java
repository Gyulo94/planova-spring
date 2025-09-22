package com.planova.server.project.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
  private final WorkspaceService workspaceService;

  @Override
  public List<ProjectResponse> findProjectsByWorkspaceId(UUID workspaceId, UUID userId) {
    workspaceMemberService.validateWorkspaceMember(workspaceId, userId);
    List<Project> projects = projectRepository.findAllByWorkspaceId(workspaceId);
    List<ProjectResponse> response = projects.stream().map(project -> ProjectResponse.fromEntity(project, imageService))
        .toList();
    return response;
  }

  @Transactional
  @Override
  public ProjectResponse createProject(ProjectRequest request, UUID userId) {
    Workspace workspace = workspaceService.getWorkspaceEntityById(request.getWorkspaceId());
    workspaceMemberService.validateWorkspaceAdmin(request.getWorkspaceId(), userId);
    Project newProject = projectRepository.save(ProjectRequest.toEntity(request, workspace));
    String newImage = imageService.createImages(newProject.getId(), List.of(request.getImage()), EntityType.PROJECT)
        .get(0);
    ProjectResponse response = ProjectResponse.fromEntity(newProject, newImage);
    return response;
  }

}
