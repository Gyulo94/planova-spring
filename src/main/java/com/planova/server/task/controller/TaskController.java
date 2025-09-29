package com.planova.server.task.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.global.api.Api;
import com.planova.server.global.message.ResponseMessage;
import com.planova.server.task.request.TaskRequest;
import com.planova.server.task.response.TaskResponse;
import com.planova.server.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @PostMapping("create")
  public Api<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
    TaskResponse response = taskService.createTask(request);
    return Api.OK(response, ResponseMessage.CREATE_TASK_SUCCESS);
  }

}
