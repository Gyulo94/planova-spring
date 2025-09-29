package com.planova.server.task.service;

import com.planova.server.task.request.TaskRequest;
import com.planova.server.task.response.TaskResponse;

public interface TaskService {

  TaskResponse createTask(TaskRequest request);

}
