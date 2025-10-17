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
import com.planova.server.workspace.request.WorkspaceRequest;
import com.planova.server.workspace.response.WorkspaceResponse;
import com.planova.server.workspace.service.WorkspaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("workspace")
@RequiredArgsConstructor
public class WorkspaceController {

  private final WorkspaceService workspaceService;

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceController.class);

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param UUID userId
   * @return List<WorkspaceResponse> (UUID id, String name)
   */
  @GetMapping("all")
  public Api<List<WorkspaceResponse>> findWorkspaces(@CurrentUser JwtPayload user) {
    List<WorkspaceResponse> response = workspaceService.findWorkspaces(user.getId());
    return Api.OK(response);
  }

  /**
   * 워크스페이스 상세 조회
   * 
   * @param UUID id, UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  @GetMapping("{id}")
  public Api<WorkspaceResponse> findWorkspaceById(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    WorkspaceResponse response = workspaceService.findWorkspaceById(id, user.getId());
    return Api.OK(response);
  }

  /**
   * 워크스페이스 생성
   * 
   * @param WorkspaceRequest (String name), UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  @PostMapping("create")
  public Api<WorkspaceResponse> createWorkspace(@Valid @RequestBody WorkspaceRequest request,
      @CurrentUser JwtPayload user) {
    WorkspaceResponse respnose = workspaceService.createWorkspace(request, user.getId());
    return Api.OK(respnose, ResponseMessage.CREATE_WORKSPACE_SUCCESS);
  }

  /**
   * 워크스페이스 수정
   * 
   * @param UUID id, WorkspaceRequest (String name), UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  @PutMapping("{id}/update")
  public Api<WorkspaceResponse> updateWorkspace(@PathVariable("id") UUID id,
      @Valid @RequestBody WorkspaceRequest request,
      @CurrentUser JwtPayload user) {
    // LOGGER.warn("워크스페이스 업데이트 컨트롤러 진입 id: {}, request: {}, user: {}", id, request,
    // user);
    WorkspaceResponse response = workspaceService.updateWorkspace(id, request, user.getId());
    return Api.OK(response, ResponseMessage.UPDATE_WORKSPACE_SUCCESS);
  }

  /**
   * 워크스페이스 삭제
   * 
   * @param UUID id, UUID userId
   * @return String (삭제 성공 메세지)
   */
  @DeleteMapping("{id}/delete")
  public Api<String> deleteWorkspace(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    workspaceService.deleteWorkspace(id, user.getId());
    return Api.OK(ResponseMessage.DELETE_WORKSPACE_SUCCESS);
  }

  /**
   * 워크스페이스 초대 코드 재발급
   * 
   * @param UUID id, UUID userId
   */
  @PutMapping("{id}/reset-invite-code")
  public Api<Void> resetInviteCode(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    workspaceService.resetInviteCode(id, user.getId());
    return Api.OK(ResponseMessage.RESET_INVITE_CODE_SUCCESS);
  }
}
