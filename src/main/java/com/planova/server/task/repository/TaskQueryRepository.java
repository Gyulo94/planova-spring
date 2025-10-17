package com.planova.server.task.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.planova.server.task.entity.Task;
import com.planova.server.task.request.TaskFilterRequest;
import com.planova.server.task.response.TaskCountResponse;

public interface TaskQueryRepository {

  List<Task> findByProjectIdAndFilters(UUID projectId, TaskFilterRequest request);

  TaskCountResponse taskCountsMonthlyByProjectId(UUID projectId, LocalDateTime start, LocalDateTime end);

  TaskCountResponse taskCountsTotalByProjectId(UUID projectId);

}
