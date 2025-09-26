package com.planova.server.project.service;

import java.util.List;
import java.util.UUID;

import com.planova.server.project.request.ProjectRequest;
import com.planova.server.project.response.ProjectResponse;

public interface ProjectService {

  List<ProjectResponse> findProjectsByWorkspaceId(UUID workspaceId, UUID userId);

  ProjectResponse createProject(ProjectRequest request, UUID userId);

  ProjectResponse findProjectById(UUID id, UUID userId);

  ProjectResponse updateProject(UUID id, ProjectRequest request, UUID userId);

  void deleteProject(UUID id, UUID userId);

}
