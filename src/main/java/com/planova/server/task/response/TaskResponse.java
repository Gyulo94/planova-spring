package com.planova.server.task.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.planova.server.image.service.ImageService;
import com.planova.server.project.response.ProjectResponse;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskPriority;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.user.response.UserResponse;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "TASK_RES_01 : 작업 응답 DTO")
public class TaskResponse {

  @Schema(description = "작업 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "작업 이름", example = "새로운 기능 추가")
  private String name;

  @Schema(description = "작업 설명", example = "사용자 로그인 기능 추가")
  private String description;

  @Schema(description = "작업 시작일", example = "2023-01-01T10:00:00")
  private LocalDateTime startDate;

  @Schema(description = "작업 마감일", example = "2023-01-31T18:00:00")
  private LocalDateTime dueDate;

  @Schema(description = "작업 상태", example = "TODO || IN_PROGRESS || COMPLETED || IN_REVIEW")
  private TaskStatus status;

  @Schema(description = "작업 우선순위", example = "LOW || MEDIUM || HIGH || CRITICAL")
  private TaskPriority priority;

  @Schema(description = "작업 담당자", example = "사용자 정보 객체")
  private UserResponse assignee;

  @Schema(description = "작업이 속한 프로젝트", example = "프로젝트 정보 객체")
  private ProjectResponse project;

  @Schema(description = "작업 생성일", example = "2023-01-01T09:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "작업 위치", example = "1000")
  private int position;

  public static TaskResponse fromEntity(Task task, ProjectResponse projectResponse, ImageService imageService) {
    return TaskResponse.builder().id(task.getId())
        .name(task.getName())
        .description(task.getDescription())
        .startDate(task.getStartDate())
        .dueDate(task.getDueDate())
        .status(task.getStatus())
        .priority(task.getPriority())
        .position(task.getPosition())
        .assignee(UserResponse.fromEntity(task.getAssignee(), imageService))
        .project(projectResponse)
        .createdAt(task.getCreatedAt())
        .build();
  }
}
