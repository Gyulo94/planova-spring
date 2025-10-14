package com.planova.server.task.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planova.server.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID>, TaskQueryRepository {

  List<Task> findByProjectId(UUID projectId);

}
