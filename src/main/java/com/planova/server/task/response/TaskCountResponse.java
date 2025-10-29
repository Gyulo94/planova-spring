package com.planova.server.task.response;

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
@Schema(title = "TASK_RES_02 : 작업 수 응답 DTO")
public class TaskCountResponse {

  @Schema(description = "총 작업 수", example = "42")
  private Long totalCount;

  @Schema(description = "할당된 작업 수", example = "10")
  private Long assignedCount;

  @Schema(description = "미완료 작업 수", example = "15")
  private Long incompleteCount;

  @Schema(description = "완료된 작업 수", example = "20")
  private Long completedCount;

  @Schema(description = "연체된 작업 수", example = "5")
  private Long inProgressCount;

  @Schema(description = "할 일 작업 수", example = "7")
  private Long todoCount;

  @Schema(description = "연체된 작업 수", example = "3")
  private Long overdueCount;

  @Schema(description = "검토 중인 작업 수", example = "4")
  private Long inReviewCount;

  @Schema(description = "백로그 작업 수", example = "6")
  private Long backlogCount;

  public TaskCountResponse(Long totalCount, Long assignedCount, Long incompleteCount, Long completedCount,
      Long overdueCount) {
    this.totalCount = totalCount == null ? 0L : totalCount;
    this.assignedCount = assignedCount == null ? 0L : assignedCount;
    this.incompleteCount = incompleteCount == null ? 0L : incompleteCount;
    this.completedCount = completedCount == null ? 0L : completedCount;
    this.overdueCount = overdueCount == null ? 0L : overdueCount;
    this.inProgressCount = 0L;
    this.todoCount = 0L;
    this.inReviewCount = 0L;
    this.backlogCount = 0L;
  }

  public TaskCountResponse getDifferencesFrom(TaskCountResponse lastMonth) {
    if (lastMonth == null) {
      return TaskCountResponse.builder()
          .totalCount(this.totalCount)
          .assignedCount(this.assignedCount)
          .incompleteCount(this.incompleteCount)
          .completedCount(this.completedCount)
          .overdueCount(this.overdueCount)
          .inProgressCount(this.inProgressCount)
          .todoCount(this.todoCount)
          .inReviewCount(this.inReviewCount)
          .backlogCount(this.backlogCount)
          .build();
    }

    return TaskCountResponse.builder()
        .totalCount(this.totalCount - lastMonth.totalCount)
        .assignedCount(this.assignedCount - lastMonth.assignedCount)
        .incompleteCount(this.incompleteCount - lastMonth.incompleteCount)
        .completedCount(this.completedCount - lastMonth.completedCount)
        .overdueCount(this.overdueCount - lastMonth.overdueCount)
        .inProgressCount(this.inProgressCount - lastMonth.inProgressCount)
        .todoCount(this.todoCount - lastMonth.todoCount)
        .inReviewCount(this.inReviewCount - lastMonth.inReviewCount)
        .backlogCount(this.backlogCount - lastMonth.backlogCount)
        .build();
  }
}
