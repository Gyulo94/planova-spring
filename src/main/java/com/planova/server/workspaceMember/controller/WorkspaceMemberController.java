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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("workspace-member")
@RequiredArgsConstructor
public class WorkspaceMemberController {
  private final WorkspaceMemberService workspaceMemberService;

  /**
   * 워크스페이스 멤버 조회
   * 
   * @param UUID workspaceId, UUID userId
   * @return WorkspaceMemberResponse
   */
  @GetMapping("{workspaceId}/members/all")
  public Api<WorkspaceMemberResponse> findWorkspaceMembers(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    WorkspaceMemberResponse response = workspaceMemberService.findWorkspaceMembers(workspaceId, user.getId());
    return Api.OK(response);
  }

  /**
   * 워크스페이스내 나의 정보 조회
   * 
   * @param UUID workspaceId, UUID userId
   * @return WorkspaceMemberInfoResponse
   */
  @GetMapping("{workspaceId}/members/me")
  public Api<WorkspaceMemberInfoResponse> findMyWorkspaceMemberInfo(@PathVariable("workspaceId") UUID workspaceId,
      @CurrentUser JwtPayload user) {
    WorkspaceMemberInfoResponse response = workspaceMemberService.findMyWorkspaceMemberInfo(workspaceId, user.getId());
    return Api.OK(response);
  }

  /**
   * 워크스페이스 참가
   * 
   * @param UUID workspaceId, WorkspaceMemberRequest request, UUID userId
   * @return message - "워크스페이스에 성공적으로 참가하였습니다."
   */
  @PostMapping("{workspaceId}/join")
  public Api<Void> joinWorkspace(@PathVariable("workspaceId") UUID workspaceId,
      @Valid @RequestBody WorkspaceMemberRequest request, @CurrentUser JwtPayload user) {
    workspaceMemberService.joinWorkspace(workspaceId, request, user.getId());
    return Api.OK(ResponseMessage.JOIN_WORKSPACE_SUCCESS);
  }

  /**
   * 워크스페이스 멤버 권한 변경
   * 
   * @param UUID workspaceId, UUID memberId, UUID userId
   * @return message - "워크스페이스 멤버 권한이 성공적으로 변경되었습니다."
   */
  @PutMapping("{workspaceId}/member/{memberId}/update")
  public Api<Void> updateWorkspaceMember(@PathVariable("workspaceId") UUID workspaceId,
      @PathVariable("memberId") UUID memberId, @CurrentUser JwtPayload user) {
    workspaceMemberService.updateWorkspaceMember(workspaceId, memberId, user.getId());
    return Api.OK(ResponseMessage.UPDATE_WORKSPACE_MEMBER_SUCCESS);
  }

  /**
   * 워크스페이스 멤버 추방
   * 
   * @param UUID workspaceId, UUID memberId, UUID userId
   * @return message - "워크스페이스 멤버가 성공적으로 추방되었습니다."
   */
  @DeleteMapping("{workspaceId}/member/{memberId}/delete")
  public Api<Void> removeWorkspaceMember(@PathVariable("workspaceId") UUID workspaceId,
      @PathVariable("memberId") UUID memberId, @CurrentUser JwtPayload user) {
    workspaceMemberService.removeWorkspaceMember(workspaceId, memberId, user.getId());
    return Api.OK(ResponseMessage.REMOVE_WORKSPACE_MEMBER_SUCCESS);
  }
}
