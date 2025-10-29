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
@Schema(title = "TASK_RES_03 : 작업 총계 응답 DTO")
public class TotalCountResponse {

        @Schema(description = "전체 작업 수")
        private CountDetail totalCount;

        @Schema(description = "할당된 작업 수")
        private CountDetail assignedCount;

        @Schema(description = "미완료 작업 수")
        private CountDetail incompleteCount;

        @Schema(description = "완료된 작업 수")
        private CountDetail completedCount;

        @Schema(description = "연체된 작업 수")
        private CountDetail overdueCount;

        @Schema(description = "할 일 작업 수")
        private CountDetail todoCount;

        @Schema(description = "진행 중인 작업 수")
        private CountDetail inProgressCount;

        @Schema(description = "검토 중인 작업 수")
        private CountDetail inReviewCount;

        @Schema(description = "백로그 작업 수")
        private CountDetail backlogCount;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @ToString
        @Schema(title = "작업 수 세부 DTO")
        public static class CountDetail {

                @Schema(description = "총 작업 수", example = "42")
                private Long total;

                @Schema(description = "전월대비 작업 수 차이", example = "5")
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
                                .todoCount(CountDetail.builder().total(totalPeriod.getTodoCount())
                                                .difference(monthlyDifferences.getTodoCount())
                                                .build())
                                .inProgressCount(CountDetail.builder().total(totalPeriod.getInProgressCount())
                                                .difference(monthlyDifferences.getInProgressCount())
                                                .build())
                                .inReviewCount(CountDetail.builder().total(totalPeriod.getInReviewCount())
                                                .difference(monthlyDifferences.getInReviewCount())
                                                .build())
                                .backlogCount(CountDetail.builder().total(totalPeriod.getBacklogCount())
                                                .difference(monthlyDifferences.getBacklogCount())
                                                .build())
                                .build();
        }
}
