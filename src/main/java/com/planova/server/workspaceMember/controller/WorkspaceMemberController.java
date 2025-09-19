package com.planova.server.workspaceMember.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.global.annotation.CurrentUser;
import com.planova.server.global.api.Api;
import com.planova.server.global.jwt.JwtPayload;
import com.planova.server.global.message.ResponseMessage;
import com.planova.server.workspaceMember.request.WorkspaceMemberRequest;
import com.planova.server.workspaceMember.response.WorkspaceMemberResponse;
import com.planova.server.workspaceMember.service.WorkspaceMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("workspace-member")
@RequiredArgsConstructor
public class WorkspaceMemberController {
  private final WorkspaceMemberService workspaceMemberService;

  @PostMapping("{workspaceId}/join")
  public Api<Void> joinWorkspace(@PathVariable("workspaceId") UUID workspaceId,
      @Valid @RequestBody WorkspaceMemberRequest request, @CurrentUser JwtPayload user) {
    workspaceMemberService.joinWorkspace(workspaceId, request, user.getId());
    return Api.OK(ResponseMessage.JOIN_WORKSPACE_SUCCESS);
  }

  @GetMapping("{workspaceId}/members/all")
  public Api<WorkspaceMemberResponse> findWorkspaceMembers(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    WorkspaceMemberResponse response = workspaceMemberService.findWorkspaceMembers(workspaceId, user.getId());
    return Api.OK(response);
  }
}
