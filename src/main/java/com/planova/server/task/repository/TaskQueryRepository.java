package com.planova.server.task.repository;

import java.util.List;
import java.util.UUID;

import com.planova.server.task.entity.Task;
import com.planova.server.task.request.TaskFilterRequest;

public interface TaskQueryRepository {

  List<Task> findByProjectIdAndFilters(UUID projectId, TaskFilterRequest request);

}
