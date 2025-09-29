package com.planova.server.task.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.project.entity.Project;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskPriority;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

  @NotBlank(message = "작업 제목을 입력하세요.")
  private String name;

  private String description;

  @NotNull(message = "담당자를 선택하세요.")
  private UUID assigneeId;

  @NotNull(message = "프로젝트 ID가 필요합니다.")
  private UUID projectId;

  @NotNull(message = "시작일이 필요합니다.")
  private LocalDateTime startDate;

  @NotNull(message = "종료일이 필요합니다.")
  private LocalDateTime dueDate;

  private TaskStatus status;
  private TaskPriority priority;

  public static Task toEntity(TaskRequest request, User assignee, Project project, int position) {
    return Task.builder()
        .name(request.getName())
        .description(request.getDescription())
        .startDate(request.getStartDate())
        .dueDate(request.getDueDate())
        .assignee(assignee)
        .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
        .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
        .project(project)
        .position(position)
        .build();
  }
}
