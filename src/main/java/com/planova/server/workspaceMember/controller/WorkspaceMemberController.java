package com.planova.server.workspaceMember.controller;

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
import com.planova.server.workspaceMember.request.WorkspaceMemberRequest;
import com.planova.server.workspaceMember.response.WorkspaceMemberInfoResponse;
import com.planova.server.workspaceMember.response.WorkspaceMemberResponse;
import com.planova.server.workspaceMember.service.WorkspaceMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("workspace-member")
@RequiredArgsConstructor
@Tag(name = "워크스페이스 멤버", description = "워크스페이스 멤버 관련 API")
public class WorkspaceMemberController {
  private final WorkspaceMemberService workspaceMemberService;

  @GetMapping("{workspaceId}/members/all")
  @Operation(summary = "워크스페이스 멤버 조회", description = "특정 워크스페이스에 속한 모든 멤버의 정보를 조회합니다.")
  public Api<WorkspaceMemberResponse> findWorkspaceMembers(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    WorkspaceMemberResponse response = workspaceMemberService.findWorkspaceMembers(workspaceId, user.getId());
    return Api.OK(response);
  }

  @GetMapping("{workspaceId}/members/me")
  @Operation(summary = "워크스페이스 내 나의 멤버 정보 조회", description = "특정 워크스페이스 내에서 나의 멤버 정보를 조회합니다.")
  public Api<WorkspaceMemberInfoResponse> findMyWorkspaceMemberInfo(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    WorkspaceMemberInfoResponse response = workspaceMemberService.findMyWorkspaceMemberInfo(workspaceId, user.getId());
    return Api.OK(response);
  }

  @PostMapping("{workspaceId}/join")
  @Operation(summary = "워크스페이스 가입", description = "특정 워크스페이스에 멤버로 가입합니다.")
  public Api<Void> joinWorkspace(@PathVariable("workspaceId") UUID workspaceId,
      @Valid @RequestBody WorkspaceMemberRequest request, @CurrentUser JwtPayload user) {
    workspaceMemberService.joinWorkspace(workspaceId, request, user.getId());
    return Api.OK(ResponseMessage.JOIN_WORKSPACE_SUCCESS);
  }

  @PutMapping("{workspaceId}/member/{memberId}/update")
  @Operation(summary = "워크스페이스 멤버 정보 수정", description = "특정 워크스페이스 멤버의 정보를 수정합니다.")
  public Api<Void> updateWorkspaceMember(@PathVariable("workspaceId") UUID workspaceId,
      @PathVariable("memberId") UUID memberId, @CurrentUser JwtPayload user) {
    workspaceMemberService.updateWorkspaceMember(workspaceId, memberId, user.getId());
    return Api.OK(ResponseMessage.UPDATE_WORKSPACE_MEMBER_SUCCESS);
  }

  @DeleteMapping("{workspaceId}/member/{memberId}/delete")
  @Operation(summary = "워크스페이스 멤버 추방", description = "특정 워크스페이스 멤버를 추방합니다.")
  public Api<Void> removeWorkspaceMember(@PathVariable("workspaceId") UUID workspaceId,
      @PathVariable("memberId") UUID memberId, @CurrentUser JwtPayload user) {
    workspaceMemberService.removeWorkspaceMember(workspaceId, memberId, user.getId());
    return Api.OK(ResponseMessage.REMOVE_WORKSPACE_MEMBER_SUCCESS);
  }
}
