package com.planova.server.task.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.project.entity.Project;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskPriority;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "TASK_REQ_01 : 작업 생성 요청 DTO")
public class TaskRequest {

  @NotBlank(message = "작업 제목을 입력하세요.")
  @Schema(description = "작업 이름", example = "디자인 작업")
  private String name;

  @Schema(description = "작업 설명", example = "웹사이트 메인 페이지 디자인 작업")
  private String description;

  @NotNull(message = "담당자를 선택하세요.")
  @Schema(description = "담당자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID assigneeId;

  @NotNull(message = "프로젝트 ID가 필요합니다.")
  @Schema(description = "프로젝트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID projectId;

  @NotNull(message = "시작일이 필요합니다.")
  @Schema(description = "시작일", example = "2024-01-01T10:00:00")
  private LocalDateTime startDate;

  @NotNull(message = "종료일이 필요합니다.")
  @Schema(description = "종료일", example = "2024-01-31T18:00:00")
  private LocalDateTime dueDate;

  @Schema(description = "작업 상태", example = "TODO || IN_PROGRESS || COMPLETED || IN_REVIEW")
  private TaskStatus status;

  @Schema(description = "작업 우선순위", example = "LOW || MEDIUM || HIGH || CRITICAL")
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
