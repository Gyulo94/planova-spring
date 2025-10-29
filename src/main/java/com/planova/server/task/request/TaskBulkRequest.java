package com.planova.server.task.request;

import java.util.UUID;

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
@Schema(title = "TASK_REQ_03 : 작업 일괄 수정 요청 DTO")
public class TaskBulkRequest {

  @Schema(description = "작업 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "작업 상태", example = "TODO || IN_PROGRESS || COMPLETED || IN_REVIEW")
  private TaskStatus status;

  @Schema(description = "작업 위치", example = "1000")
  private int position;

}
