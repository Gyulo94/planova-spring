package com.planova.server.workspace.service;

import java.util.List;
import java.util.UUID;

import com.planova.server.workspace.request.WorkspaceRequest;
import com.planova.server.workspace.response.WorkspaceResponse;

public interface WorkspaceService {

  List<WorkspaceResponse> findWorkspaces(UUID userId);

  /**
   * 워크스페이스 생성
   * 
   * @param WorkspaceRequest (String name), UUID userId
   * @return WorkspaceResponse (UUID id, String name)
   */
  WorkspaceResponse createWorkspace(WorkspaceRequest request, UUID userId);

}
