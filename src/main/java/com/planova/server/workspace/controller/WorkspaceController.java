package com.planova.server.workspace.controller;

import org.springframework.web.bind.annotation.PostMapping;
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

}
