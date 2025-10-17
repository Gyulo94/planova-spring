package com.planova.server.task.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planova.server.global.annotation.CurrentUser;
import com.planova.server.global.api.Api;
import com.planova.server.global.jwt.JwtPayload;
import com.planova.server.global.message.ResponseMessage;
import com.planova.server.task.request.TaskBulkRequest;
import com.planova.server.task.request.TaskFilterRequest;
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

  Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

  @PostMapping("create")
  public Api<TaskResponse> createTask(@Valid @RequestBody TaskRequest request, @CurrentUser JwtPayload user) {
    TaskResponse response = taskService.createTask(request, user.getId());
    return Api.OK(response, ResponseMessage.CREATE_TASK_SUCCESS);
  }

  @GetMapping("{projectId}/all")
  public Api<List<TaskResponse>> findTasks(@PathVariable("projectId") UUID projectId,
      @ModelAttribute TaskFilterRequest request) {
    // LOGGER.info("request: {}", request);
    List<TaskResponse> responses = taskService.findTasks(projectId, request);
    // LOGGER.info("response: {}", responses);
    return Api.OK(responses);
  }

  @GetMapping("{id}")
  public Api<TaskResponse> findTask(@PathVariable("id") UUID id, @CurrentUser JwtPayload user) {
    TaskResponse response = taskService.findTask(id, user.getId());
    return Api.OK(response);
  }

  @PutMapping("{id}/update")
  public Api<TaskResponse> updateTask(@PathVariable("id") UUID id,
      @Valid @RequestBody TaskRequest request, @CurrentUser JwtPayload user) {
    TaskResponse response = taskService.updateTask(id, request, user.getId());
    return Api.OK(response, ResponseMessage.UPDATE_TASK_SUCCESS);
  }

  @PutMapping("bulk-update")
  public Api<Void> bulkUpdateTasks(@Valid @RequestBody List<TaskBulkRequest> requests,
      @CurrentUser JwtPayload user) {
    taskService.bulkUpdateTasks(requests, user.getId());
    return Api.OK(ResponseMessage.UPDATE_TASK_SUCCESS);
  }

  @DeleteMapping("{id}/delete")
  public Api<Void> deleteTask(@PathVariable("id") UUID id,
      @CurrentUser JwtPayload user) {
    taskService.deleteTask(id, user.getId());
    return Api.OK(ResponseMessage.DELETE_TASK_SUCCESS);
  }

}
