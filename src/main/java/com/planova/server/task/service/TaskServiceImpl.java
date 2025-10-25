package com.planova.server.task.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.global.util.DateUtils;
import com.planova.server.image.service.ImageService;
import com.planova.server.project.entity.Project;
import com.planova.server.project.response.ProjectResponse;
import com.planova.server.project.service.ProjectService;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.task.repository.TaskRepository;
import com.planova.server.task.request.TaskBulkRequest;
import com.planova.server.task.request.TaskFilterRequest;
import com.planova.server.task.request.TaskRequest;
import com.planova.server.task.response.TaskCountResponse;
import com.planova.server.task.response.TaskResponse;
import com.planova.server.task.response.TotalCountResponse;
import com.planova.server.user.entity.User;
import com.planova.server.user.service.UserService;
import com.planova.server.workspace.entity.Workspace;
import com.planova.server.workspace.service.WorkspaceService;
import com.planova.server.workspaceMember.service.WorkspaceMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final UserService userService;
  private final ProjectService projectService;
  private final ImageService imageService;
  private final WorkspaceMemberService workspaceMemberService;
  private final WorkspaceService workspaceService;

  @Transactional
  @Override
  public TaskResponse createTask(TaskRequest request, UUID userId) {
    User assignee = userService.getUserEntityById(request.getAssigneeId());
    Project project = projectService.getProjectEntityById(request.getProjectId());
    int position = getNextPosition(project.getId(), request.getStatus());
    Task newTask = taskRepository.save(TaskRequest.toEntity(request, assignee, project, position));
    ProjectResponse projectResponse = ProjectResponse.fromEntity(project, imageService);
    TaskResponse response = TaskResponse.fromEntity(newTask, projectResponse, imageService);
    return response;
  }

  private int getNextPosition(UUID projectId, TaskStatus status) {
    List<Task> tasks = taskRepository.findByProjectId(projectId);
    Optional<Task> lastTask = tasks.stream()
        .filter(task -> status.equals(task.getStatus()))
        .max(Comparator.comparingInt(Task::getPosition));
    return lastTask.map(task -> task.getPosition() + 1000).orElse(1000);
  }

  @Override
  public List<TaskResponse> findTasks(UUID projectId, TaskFilterRequest request) {
    Project project = projectService.getProjectEntityById(projectId);
    List<Task> tasks = taskRepository.findByProjectIdAndFilters(projectId, request);
    ProjectResponse projectResponse = ProjectResponse.fromEntity(project, imageService);
    List<TaskResponse> responses = tasks.stream()
        .map(task -> TaskResponse.fromEntity(task, projectResponse, imageService))
        .toList();
    return responses;
  }

  @Override
  public List<TaskResponse> findMyTasksByWorkspace(UUID workspaceId, TaskFilterRequest request, UUID userId) {
    Workspace workspace = workspaceService.getWorkspaceEntityById(workspaceId);
    workspaceMemberService.validateWorkspaceMember(workspace.getId(), userId);
    List<Task> tasks = taskRepository.findByWorkspaceIdAndFilters(workspaceId, request, userId);
    List<TaskResponse> responses = tasks.stream()
        .map(task -> {
          ProjectResponse projectResponse = ProjectResponse.fromEntity(task.getProject(), imageService);
          return TaskResponse.fromEntity(task, projectResponse, imageService);
        })
        .toList();
    return responses;
  }

  @Override
  public List<TaskResponse> findTasksByWorkspace(UUID workspaceId, TaskFilterRequest request, UUID userId) {
    Workspace workspace = workspaceService.getWorkspaceEntityById(workspaceId);
    workspaceMemberService.validateWorkspaceMember(workspace.getId(), userId);
    List<Task> tasks = taskRepository.findByWorkspaceIdAndFilters(workspaceId, request, null);
    List<TaskResponse> responses = tasks.stream()
        .map(task -> {
          ProjectResponse projectResponse = ProjectResponse.fromEntity(task.getProject(), imageService);
          return TaskResponse.fromEntity(task, projectResponse, imageService);
        })
        .toList();
    return responses;
  }

  private Task findTaskEntityById(UUID id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
    return task;
  }

  @Override
  public Task getTaskEntityById(UUID id) {
    return findTaskEntityById(id);
  }

  @Override
  public TaskResponse findTask(UUID id, UUID userId) {
    Task task = findTaskEntityById(id);
    UUID workspaceId = task.getProject().getWorkspace().getId();
    workspaceMemberService.validateWorkspaceMember(workspaceId, userId);
    ProjectResponse projectResponse = ProjectResponse.fromEntity(task.getProject(), imageService);
    TaskResponse response = TaskResponse.fromEntity(task, projectResponse, imageService);
    return response;
  }

  @Transactional
  @Override
  public TaskResponse updateTask(UUID id, TaskRequest request, UUID userId) {
    Task task = findTaskEntityById(id);
    Project project = projectService.getProjectEntityById(request.getProjectId());

    UUID workspaceId = task.getProject().getWorkspace().getId();
    workspaceMemberService.validateWorkspaceMember(workspaceId, userId);
    boolean isAssignee = task.getAssignee().getId().equals(userId);

    User assignee = userService.getUserEntityById(request.getAssigneeId());

    ProjectResponse projectResponse = ProjectResponse.fromEntity(project, imageService);

    if (!isAssignee) {
      workspaceMemberService.validateWorkspaceAdmin(workspaceId, userId);
      task.update(request, assignee);
      TaskResponse response = TaskResponse.fromEntity(task, projectResponse, imageService);
      return response;
    } else {
      task.update(request, assignee);
      TaskResponse response = TaskResponse.fromEntity(task, projectResponse, imageService);
      return response;
    }
  }

  @Transactional
  @Override
  public void bulkUpdateTasks(List<TaskBulkRequest> requests, UUID userId) {
    for (TaskBulkRequest request : requests) {
      Task task = findTaskEntityById(request.getId());
      UUID workspaceId = task.getProject().getWorkspace().getId();
      workspaceMemberService.validateWorkspaceMember(workspaceId, userId);
      boolean isAssignee = task.getAssignee().getId().equals(userId);

      if (!isAssignee) {
        workspaceMemberService.validateWorkspaceAdmin(workspaceId, userId);
        task.updateStatusOrPosition(request);
      } else {
        task.updateStatusOrPosition(request);
      }
    }
  }

  @Override
  public void deleteTask(UUID id, UUID userId) {
    Task task = findTaskEntityById(id);
    UUID workspaceId = task.getProject().getWorkspace().getId();
    workspaceMemberService.validateWorkspaceMember(workspaceId, userId);
    boolean isAssignee = task.getAssignee().getId().equals(userId);
    if (!isAssignee) {
      workspaceMemberService.validateWorkspaceAdmin(workspaceId, userId);
      taskRepository.deleteById(id);
    } else {
      taskRepository.deleteById(id);
    }
  }

  @Override
  public TotalCountResponse findTaskCountsByProjectId(UUID projectId, UUID userId) {
    Project project = projectService.getProjectEntityById(projectId);
    workspaceMemberService.validateWorkspaceMember(project.getWorkspace().getId(), userId);

    DateUtils.DateRange thisMonth = DateUtils.thisMonthRange();
    DateUtils.DateRange lastMonth = DateUtils.lastMonthRange();

    LocalDateTime thisStart = thisMonth.getStart();
    LocalDateTime thisEnd = thisMonth.getEnd();

    LocalDateTime lastStart = lastMonth.getStart();
    LocalDateTime lastEnd = lastMonth.getEnd();

    TaskCountResponse thisMonthCounts = taskRepository.taskCountsMonthlyByProjectId(projectId, thisStart, thisEnd);
    TaskCountResponse lastMonthCounts = taskRepository.taskCountsMonthlyByProjectId(projectId, lastStart, lastEnd);
    TaskCountResponse totalPeriodCounts = taskRepository.taskCountsTotalByProjectId(projectId);

    TotalCountResponse response = TotalCountResponse.from(thisMonthCounts, lastMonthCounts, totalPeriodCounts);

    return response;
  }

  @Override
  public TotalCountResponse findMyTaskCountsByWorkspaceId(UUID workspaceId, UUID userId) {
    Workspace workspace = workspaceService.getWorkspaceEntityById(workspaceId);
    workspaceMemberService.validateWorkspaceMember(workspace.getId(), userId);

    DateUtils.DateRange thisMonth = DateUtils.thisMonthRange();
    DateUtils.DateRange lastMonth = DateUtils.lastMonthRange();

    LocalDateTime thisStart = thisMonth.getStart();
    LocalDateTime thisEnd = thisMonth.getEnd();

    LocalDateTime lastStart = lastMonth.getStart();
    LocalDateTime lastEnd = lastMonth.getEnd();

    TaskCountResponse thisMonthCounts = taskRepository.myTaskCountsMonthlyByWorkspaceId(workspaceId, thisStart, thisEnd,
        userId);
    TaskCountResponse lastMonthCounts = taskRepository.myTaskCountsMonthlyByWorkspaceId(workspaceId, lastStart, lastEnd,
        userId);
    TaskCountResponse totalPeriodCounts = taskRepository.myTaskCountsTotalByWorkspaceId(workspaceId, userId);

    TotalCountResponse response = TotalCountResponse.from(thisMonthCounts, lastMonthCounts, totalPeriodCounts);

    return response;
  }

  @Override
  public TotalCountResponse findTaskCountsByWorkspaceId(UUID workspaceId, UUID userId) {
    Workspace workspace = workspaceService.getWorkspaceEntityById(workspaceId);
    workspaceMemberService.validateWorkspaceMember(workspace.getId(), userId);

    DateUtils.DateRange thisMonth = DateUtils.thisMonthRange();
    DateUtils.DateRange lastMonth = DateUtils.lastMonthRange();

    LocalDateTime thisStart = thisMonth.getStart();
    LocalDateTime thisEnd = thisMonth.getEnd();

    LocalDateTime lastStart = lastMonth.getStart();
    LocalDateTime lastEnd = lastMonth.getEnd();

    TaskCountResponse thisMonthCounts = taskRepository.taskCountsMonthlyByWorkspaceId(workspaceId, thisStart, thisEnd);
    TaskCountResponse lastMonthCounts = taskRepository.taskCountsMonthlyByWorkspaceId(workspaceId, lastStart, lastEnd);
    TaskCountResponse totalPeriodCounts = taskRepository.taskCountsTotalByWorkspaceId(workspaceId);

    TotalCountResponse response = TotalCountResponse.from(thisMonthCounts, lastMonthCounts, totalPeriodCounts);

    return response;
  }

}
