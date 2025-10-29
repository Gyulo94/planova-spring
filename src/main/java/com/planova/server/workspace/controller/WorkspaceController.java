package com.planova.server.workspace.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.planova.server.task.response.TotalCountResponse;
import com.planova.server.task.service.TaskService;
import com.planova.server.workspace.request.WorkspaceRequest;
import com.planova.server.workspace.response.WorkspaceResponse;
import com.planova.server.workspace.service.WorkspaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("workspace")
@RequiredArgsConstructor
@Tag(name = "워크스페이스", description = "워크스페이스 관련 API")
public class WorkspaceController {

  private final WorkspaceService workspaceService;
  private final TaskService taskService;

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceController.class);

  @GetMapping("all")
  @Operation(summary = "사용자가 속한 모든 워크스페이스 조회", description = "특정 사용자가 속한 모든 워크스페이스를 조회합니다.")
  public Api<List<WorkspaceResponse>> findWorkspaces(@CurrentUser JwtPayload user) {
    List<WorkspaceResponse> response = workspaceService.findWorkspaces(user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}")
  @Operation(summary = "워크스페이스 조회", description = "특정 ID를 가진 워크스페이스의 정보를 조회합니다.")
  public Api<WorkspaceResponse> findWorkspaceById(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    WorkspaceResponse response = workspaceService.findWorkspaceById(id, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}/analytics")
  @Operation(summary = "워크스페이스 분석 정보 조회", description = "특정 ID를 가진 워크스페이스의 분석 정보를 조회합니다.")
  public Api<TotalCountResponse> findTaskCountsByWorkspaceId(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    TotalCountResponse response = taskService.findTaskCountsByWorkspaceId(id, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{id}/my-analytics")
  @Operation(summary = "내 작업 분석 정보 조회", description = "특정 ID를 가진 워크스페이스에서 나의 작업 분석 정보를 조회합니다.")
  public Api<TotalCountResponse> findMyTaskCountsByWorkspaceId(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    TotalCountResponse response = taskService.findMyTaskCountsByWorkspaceId(id, user.getId());
    return Api.OK(response);
  }

  @PostMapping("create")
  @Operation(summary = "워크스페이스 생성", description = "새로운 워크스페이스를 생성합니다.")
  public Api<WorkspaceResponse> createWorkspace(@Valid @RequestBody WorkspaceRequest request,
      @CurrentUser JwtPayload user) {
    WorkspaceResponse respnose = workspaceService.createWorkspace(request, user.getId());
    return Api.OK(respnose, ResponseMessage.CREATE_WORKSPACE_SUCCESS);
  }

  @PutMapping("{id}/update")
  @Operation(summary = "워크스페이스 수정", description = "특정 ID를 가진 워크스페이스의 정보를 수정합니다.")
  public Api<WorkspaceResponse> updateWorkspace(@PathVariable("id") UUID id,
      @Valid @RequestBody WorkspaceRequest request,
      @CurrentUser JwtPayload user) {
    // LOGGER.warn("워크스페이스 업데이트 컨트롤러 진입 id: {}, request: {}, user: {}", id, request,
    // user);
    WorkspaceResponse response = workspaceService.updateWorkspace(id, request, user.getId());
    return Api.OK(response, ResponseMessage.UPDATE_WORKSPACE_SUCCESS);
  }

  @DeleteMapping("{id}/delete")
  @Operation(summary = "워크스페이스 삭제", description = "특정 ID를 가진 워크스페이스를 삭제합니다.")
  public Api<String> deleteWorkspace(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    workspaceService.deleteWorkspace(id, user.getId());
    return Api.OK(ResponseMessage.DELETE_WORKSPACE_SUCCESS);
  }

  @PutMapping("{id}/reset-invite-code")
  @Operation(summary = "워크스페이스 초대 코드 재설정", description = "특정 ID를 가진 워크스페이스의 초대 코드를 재설정합니다.")
  public Api<Void> resetInviteCode(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    workspaceService.resetInviteCode(id, user.getId());
    return Api.OK(ResponseMessage.RESET_INVITE_CODE_SUCCESS);
  }
}
