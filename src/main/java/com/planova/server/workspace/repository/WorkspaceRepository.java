package com.planova.server.workspace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planova.server.user.entity.User;
import com.planova.server.workspace.entity.Workspace;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

  /**
   * 특정 사용자의 모든 워크스페이스 조회
   * 
   * @param user 사용자 엔티티
   * @return 해당 사용자의 워크스페이스 목록
   */
  List<Workspace> findAllByUser(User user);

}
