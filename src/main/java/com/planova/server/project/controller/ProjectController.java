package com.planova.server.project.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.global.annotation.CurrentUser;
import com.planova.server.global.api.Api;
import com.planova.server.global.jwt.JwtPayload;
import com.planova.server.global.message.ResponseMessage;
import com.planova.server.project.request.ProjectRequest;
import com.planova.server.project.response.ProjectResponse;
import com.planova.server.project.service.ProjectService;
import com.planova.server.task.response.TotalCountResponse;
import com.planova.server.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectService projectService;
  private final TaskService taskService;

  @GetMapping("{workspaceId}/all")
  public Api<List<ProjectResponse>> findProjectsByWorkspaceId(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    List<ProjectResponse> response = projectService.findProjectsByWorkspaceId(workspaceId, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}")
  public Api<ProjectResponse> findProjectById(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    ProjectResponse response = projectService.findProjectById(id, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}/analytics")
  public Api<TotalCountResponse> findTaskCountsByProjectId(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    TotalCountResponse response = taskService.findTaskCountsByProjectId(id, user.getId());
    return Api.OK(response);
  }

  @PostMapping("create")
  public Api<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request,
      @CurrentUser JwtPayload user) {
    ProjectResponse respnose = projectService.createProject(request, user.getId());
    return Api.OK(respnose, ResponseMessage.CREATE_PROJECT_SUCCESS);
  }

  @PutMapping("{id}/update")
  public Api<ProjectResponse> updateProject(@PathVariable("id") UUID id,
      @Valid @RequestBody ProjectRequest request,
      @CurrentUser JwtPayload user) {
    ProjectResponse response = projectService.updateProject(id, request, user.getId());
    return Api.OK(response, ResponseMessage.UPDATE_PROJECT_SUCCESS);
  }

  @DeleteMapping("{id}/delete")
  public Api<String> deleteProject(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    projectService.deleteProject(id, user.getId());
    return Api.OK(ResponseMessage.DELETE_PROJECT_SUCCESS);
  }
}
