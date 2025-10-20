package com.planova.server.task.service;

import java.util.List;
import java.util.UUID;

import com.planova.server.task.request.TaskBulkRequest;
import com.planova.server.task.request.TaskFilterRequest;
import com.planova.server.task.request.TaskRequest;
import com.planova.server.task.response.TaskResponse;
import com.planova.server.task.response.TotalCountResponse;

public interface TaskService {

  TaskResponse createTask(TaskRequest request, UUID userId);

  List<TaskResponse> findTasks(UUID projectId, TaskFilterRequest request);

  TaskResponse findTask(UUID id, UUID userId);

  TaskResponse updateTask(UUID id, TaskRequest request, UUID userId);

  void deleteTask(UUID id, UUID userId);

  void bulkUpdateTasks(List<TaskBulkRequest> requests, UUID userId);

  TotalCountResponse findTaskCountsByProjectId(UUID projectId, UUID userId);

  TotalCountResponse findTaskCountsByWorkspaceId(UUID workspaceId, UUID userId);

  List<TaskResponse> findTasksByWorkspace(UUID workspaceId, TaskFilterRequest request, UUID id);

}
