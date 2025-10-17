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
public class TotalCountResponse {

    private CountDetail totalCount;
    private CountDetail assignedCount;
    private CountDetail incompleteCount;
    private CountDetail completedCount;
    private CountDetail overdueCount;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CountDetail {
        private Long total;
        private Long difference;
    }

    public static TotalCountResponse from(
            TaskCountResponse thisMonth,
            TaskCountResponse lastMonth,
            TaskCountResponse totalPeriod) {

        TaskCountResponse monthlyDifferences = thisMonth.getDifferencesFrom(lastMonth);

        return TotalCountResponse.builder()
                .totalCount(CountDetail.builder()
                        .total(totalPeriod.getTotalCount())
                        .difference(monthlyDifferences.getTotalCount())
                        .build())
                .assignedCount(CountDetail.builder()
                        .total(totalPeriod.getAssignedCount())
                        .difference(monthlyDifferences.getAssignedCount())
                        .build())
                .incompleteCount(CountDetail.builder()
                        .total(totalPeriod.getIncompleteCount())
                        .difference(monthlyDifferences.getIncompleteCount())
                        .build())
                .completedCount(CountDetail.builder()
                        .total(totalPeriod.getCompletedCount())
                        .difference(monthlyDifferences.getCompletedCount())
                        .build())
                .overdueCount(CountDetail.builder()
                        .total(totalPeriod.getOverdueCount())
                        .difference(monthlyDifferences.getOverdueCount())
                        .build())
                .build();
    }
}
