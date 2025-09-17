package com.planova.server.workspace.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planova.server.workspace.entity.Workspace;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

}
