package com.planova.server.task.response;

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
public class TaskCountResponse {
  private Long totalCount;
  private Long assignedCount;
  private Long incompleteCount;
  private Long completedCount;
  private Long inProgressCount;
  private Long todoCount;
  private Long overdueCount;
  private Long inReviewCount;
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
