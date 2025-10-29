package com.planova.server.task.request;

import java.util.UUID;

import com.planova.server.task.entity.TaskPriority;
import com.planova.server.task.entity.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "TASK_REQ_02 : 작업 필터 요청 DTO")
public class TaskFilterRequest {

  @Schema(description = "작업 상태", example = "TODO || IN_PROGRESS || COMPLETED || IN_REVIEW")
  private TaskStatus status;

  @Schema(description = "작업 우선순위", example = "LOW || MEDIUM || HIGH || CRITICAL")
  private TaskPriority priority;

  @Schema(description = "담당자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID assigneeId;

  @Schema(description = "프로젝트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID projectId;

  @Schema(description = "검색어", example = "버그 수정")
  private String search;

  @Schema(description = "시작 날짜", example = "2024-01-01")
  private String startDate;

  @Schema(description = "마감 날짜", example = "2024-12-31")
  private String dueDate;

  @Schema(description = "건너뛸 항목 수", example = "0")
  private int take;
}
