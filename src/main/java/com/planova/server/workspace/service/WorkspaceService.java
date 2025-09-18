package com.planova.server.workspace.service;

import java.util.List;
import java.util.UUID;

import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspace.request.WorkspaceRequest;
import com.planova.server.workspace.response.WorkspaceResponse;

public interface WorkspaceService {

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param UUID userId
   * @return List<WorkspaceResponse> (UUID id, String name, String image)
   */
  List<WorkspaceResponse> findWorkspaces(UUID userId);

  /**
   * 워크스페이스 상세 조회
   * 
   * @param UUID id, UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  WorkspaceResponse findWorkspaceById(UUID id, UUID userId);

  /**
   * 워크스페이스 엔터티 반환 메서드 (공개)
   */
  Workspace getWorkspaceEntityById(UUID id);

  /**
   * 워크스페이스 생성
   * 
   * @param WorkspaceRequest (String name), UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  WorkspaceResponse createWorkspace(WorkspaceRequest request, UUID userId);

  /**
   * 워크스페이스 수정
   * 
   * @param UUID id, WorkspaceRequest (String name), UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  WorkspaceResponse updateWorkspace(UUID id, WorkspaceRequest request, UUID userId);

  /**
   * 워크스페이스 삭제
   * 
   * @param UUID id, UUID userId
   * @return String (삭제 성공 메시지)
   */
  void deleteWorkspace(UUID id, UUID userId);

  /**
   * 워크스페이스 초대 코드 재발급
   * 
   * @param UUID id, UUID userId
   */
  void resetInviteCode(UUID id, UUID userId);
}
