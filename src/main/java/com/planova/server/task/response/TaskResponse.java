package com.planova.server.task.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.project.response.ProjectResponse;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskPriority;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.user.response.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
  private UUID id;
  private String name;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime dueDate;
  private TaskStatus status;
  private TaskPriority priority;
  private UserResponse assignee;
  private ProjectResponse project;
  private int position;

  public static TaskResponse fromEntity(Task task, ProjectResponse projectResponse) {
    return TaskResponse.builder().id(task.getId())
        .name(task.getName())
        .description(task.getDescription())
        .startDate(task.getStartDate())
        .dueDate(task.getDueDate())
        .status(task.getStatus())
        .priority(task.getPriority())
        .position(task.getPosition())
        .assignee(UserResponse.fromEntity(task.getAssignee()))
        .project(projectResponse)
        .build();
  }
}
