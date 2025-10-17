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
  private Long overdueCount;

  public TaskCountResponse getDifferencesFrom(TaskCountResponse lastMonth) {
    if (lastMonth == null) {
      return TaskCountResponse.builder()
          .totalCount(this.totalCount)
          .assignedCount(this.assignedCount)
          .incompleteCount(this.incompleteCount)
          .completedCount(this.completedCount)
          .overdueCount(this.overdueCount)
          .build();
    }

    return TaskCountResponse.builder()
        .totalCount(this.totalCount - lastMonth.totalCount)
        .assignedCount(this.assignedCount - lastMonth.assignedCount)
        .incompleteCount(this.incompleteCount - lastMonth.incompleteCount)
        .completedCount(this.completedCount - lastMonth.completedCount)
        .overdueCount(this.overdueCount - lastMonth.overdueCount)
        .build();
  }
}
