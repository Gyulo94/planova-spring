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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
@Tag(name = "프로젝트", description = "프로젝트 관련 API")
public class ProjectController {

  private final ProjectService projectService;
  private final TaskService taskService;

  @GetMapping("{workspaceId}/all")
  @Operation(summary = "워크스페이스 내 모든 프로젝트 조회", description = "특정 워크스페이스에 속한 모든 프로젝트를 조회합니다.")
  public Api<List<ProjectResponse>> findProjectsByWorkspaceId(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    List<ProjectResponse> response = projectService.findProjectsByWorkspaceId(workspaceId, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}")
  @Operation(summary = "프로젝트 조회", description = "특정 ID를 가진 프로젝트의 정보를 조회합니다.")
  public Api<ProjectResponse> findProjectById(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    ProjectResponse response = projectService.findProjectById(id, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}/analytics")
  @Operation(summary = "프로젝트 분석 정보 조회", description = "특정 ID를 가진 프로젝트의 분석 정보를 조회합니다.")
  public Api<TotalCountResponse> findTaskCountsByProjectId(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    TotalCountResponse response = taskService.findTaskCountsByProjectId(id, user.getId());
    return Api.OK(response);
  }

  @PostMapping("create")
  @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
  public Api<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request,
      @CurrentUser JwtPayload user) {
    ProjectResponse respnose = projectService.createProject(request, user.getId());
    return Api.OK(respnose, ResponseMessage.CREATE_PROJECT_SUCCESS);
  }

  @PutMapping("{id}/update")
  @Operation(summary = "프로젝트 수정", description = "특정 ID를 가진 프로젝트의 정보를 수정합니다.")
  public Api<ProjectResponse> updateProject(@PathVariable("id") UUID id,
      @Valid @RequestBody ProjectRequest request,
      @CurrentUser JwtPayload user) {
    ProjectResponse response = projectService.updateProject(id, request, user.getId());
    return Api.OK(response, ResponseMessage.UPDATE_PROJECT_SUCCESS);
  }

  @DeleteMapping("{id}/delete")
  @Operation(summary = "프로젝트 삭제", description = "특정 ID를 가진 프로젝트를 삭제합니다.")
  public Api<String> deleteProject(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    projectService.deleteProject(id, user.getId());
    return Api.OK(ResponseMessage.DELETE_PROJECT_SUCCESS);
  }
}
