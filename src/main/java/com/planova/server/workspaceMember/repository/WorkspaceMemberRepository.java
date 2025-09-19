package com.planova.server.workspaceMember.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planova.server.workspaceMember.entity.WorkspaceMember;
import com.planova.server.workspaceMember.entity.WorkspaceMemberId;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {

  /**
   * 유저가 속한 워크스페이스 조회
   * 
   * @param User user
   * @return List<WorkspaceMember>
   */
  List<WorkspaceMember> findAllByUserId(UUID userId);

  /**
   * 워크스페이스에 속한 모든 멤버 조회
   * 
   * @param Workspace workspace
   * @return List<WorkspaceMember>
   */
  List<WorkspaceMember> findAllByWorkspaceId(UUID workspaceId);
}
