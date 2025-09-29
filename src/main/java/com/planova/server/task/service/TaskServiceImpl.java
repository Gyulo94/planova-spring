package com.planova.server.task.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planova.server.image.service.ImageService;
import com.planova.server.project.entity.Project;
import com.planova.server.project.response.ProjectResponse;
import com.planova.server.project.service.ProjectService;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.task.repository.TaskRepository;
import com.planova.server.task.request.TaskRequest;
import com.planova.server.task.response.TaskResponse;
import com.planova.server.user.entity.User;
import com.planova.server.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final UserService userService;
  private final ProjectService projectService;
  private final ImageService imageService;

  @Override
  public TaskResponse createTask(TaskRequest request) {
    User assignee = userService.getUserEntityById(request.getAssigneeId());
    Project project = projectService.getProjectEntityById(request.getProjectId());
    int position = getNextPosition(project.getId(), request.getStatus());
    Task newTask = taskRepository.save(TaskRequest.toEntity(request, assignee, project, position));
    ProjectResponse projectResponse = ProjectResponse.fromEntity(project, imageService);
    TaskResponse response = TaskResponse.fromEntity(newTask, projectResponse);
    return response;
  }

  private int getNextPosition(UUID projectId, TaskStatus status) {
    List<Task> tasks = taskRepository.findByProjectId(projectId);
    Optional<Task> lastTask = tasks.stream()
        .filter(task -> status.equals(task.getStatus()))
        .max(Comparator.comparingInt(Task::getPosition));
    return lastTask.map(task -> task.getPosition() + 1000).orElse(1000);
  }

}
