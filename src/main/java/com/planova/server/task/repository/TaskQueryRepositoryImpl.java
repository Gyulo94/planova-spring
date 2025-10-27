package com.planova.server.task.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.planova.server.task.entity.QTask;
import com.planova.server.task.entity.Task;
import com.planova.server.task.entity.TaskStatus;
import com.planova.server.task.request.TaskFilterRequest;
import com.planova.server.task.response.TaskCountResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

  private final JPAQueryFactory queryBuilder;
  private final LocalDateTime CURRENT_DATE = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
  private final QTask task = QTask.task;

  @Override
  public List<Task> findByProjectIdAndFilters(UUID projectId, TaskFilterRequest request) {
    List<BooleanExpression> predicates = new ArrayList<>();

    predicates.add(task.project.id.eq(projectId));
    addIfNotNull(predicates, request.getStatus(), v -> task.status.eq(v));
    addIfNotNull(predicates, request.getPriority(), v -> task.priority.eq(v));
    addIfNotNull(predicates, request.getAssigneeId(), v -> task.assignee.id.eq(v));
    addIfNotNull(predicates, parseDateTime(request.getStartDate()), v -> task.startDate.goe(v));
    addIfNotNull(predicates, parseDateTime(request.getDueDate()), v -> task.dueDate.goe(v));
    addIfNotNull(predicates, request.getSearch(), v -> task.name.containsIgnoreCase(v));

    return queryBuilder.selectFrom(task)
        .where(predicates.toArray(new BooleanExpression[0]))
        .fetch();
  }

  @Override
  public List<Task> findByWorkspaceIdAndFilters(UUID workspaceId, TaskFilterRequest request, UUID userId) {
    List<BooleanExpression> predicates = new ArrayList<>();

    predicates.add(task.project.workspace.id.eq(workspaceId));
    addIfNotNull(predicates, request.getStatus(), v -> task.status.eq(v));
    addIfNotNull(predicates, request.getPriority(), v -> task.priority.eq(v));
    addIfNotNull(predicates, request.getAssigneeId(), v -> task.assignee.id.eq(v));
    addIfNotNull(predicates, request.getProjectId(), v -> task.project.id.eq(v));
    addIfNotNull(predicates, parseDateTime(request.getStartDate()), v -> task.startDate.goe(v));
    addIfNotNull(predicates, parseDateTime(request.getDueDate()), v -> task.dueDate.goe(v));
    addIfNotNull(predicates, request.getSearch(), v -> task.name.containsIgnoreCase(v));
    if (userId != null) {
      predicates.add(task.assignee.id.eq(userId));
    }

    var query = queryBuilder.selectFrom(task)
        .where(predicates.toArray(new BooleanExpression[0]))
        .orderBy(task.createdAt.desc());
    if (request.getTake() > 0) {
      query = query.limit(request.getTake());
    }
    return query.fetch();
  }

  private <T> void addIfNotNull(List<BooleanExpression> predicates, T value,
      java.util.function.Function<T, BooleanExpression> expr) {
    if (value != null) {
      predicates.add(expr.apply(value));
    }
  }

  private LocalDateTime parseDateTime(String dateTimeStr) {
    if (dateTimeStr == null)
      return null;
    try {
      return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    } catch (Exception e) {
      try {
        return LocalDate.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
      } catch (Exception ex) {
        return null;
      }
    }
  }

  @Override
  public TaskCountResponse taskCountsMonthlyByProjectId(UUID projectId, LocalDateTime start, LocalDateTime end) {
    return queryBuilder
        .select(Projections.constructor(TaskCountResponse.class,
            createTotalCountExpression().as("totalCount"),
            createAssignedCountExpression().as("assignedCount"),
            createIncompleteCountExpression().as("incompleteCount"),
            createCompleteCountExpression().as("completedCount"),
            createOverdueCountExpression().as("overdueCount")))
        .from(task)
        .where(task.project.id.eq(projectId)
            .and(task.createdAt.between(start, end)))
        .fetchOne();
  }

  @Override
  public TaskCountResponse taskCountsTotalByProjectId(UUID projectId) {
    return queryBuilder
        .select(Projections.constructor(TaskCountResponse.class,
            createTotalCountExpression().as("totalCount"),
            createAssignedCountExpression().as("assignedCount"),
            createIncompleteCountExpression().as("incompleteCount"),
            createCompleteCountExpression().as("completedCount"),
            createOverdueCountExpression().as("overdueCount")))
        .from(task)
        .where(task.project.id.eq(projectId))
        .fetchOne();
  }

  @Override
  public TaskCountResponse myTaskCountsMonthlyByWorkspaceId(UUID workspaceId, LocalDateTime start,
      LocalDateTime end, UUID userId) {
    return queryBuilder
        .select(Projections.constructor(TaskCountResponse.class,
            createTotalCountExpression().as("totalCount"),
            createAssignedCountExpression().as("assignedCount"),
            createIncompleteCountExpression().as("incompleteCount"),
            createCompleteCountExpression().as("completedCount"),
            createOverdueCountExpression().as("overdueCount")))
        .from(task)
        .where(task.project.workspace.id.eq(workspaceId)
            .and(task.createdAt.between(start, end))
            .and(task.assignee.id.eq(userId)))
        .fetchOne();
  }

  @Override
  public TaskCountResponse myTaskCountsTotalByWorkspaceId(UUID workspaceId, UUID userId) {
    return queryBuilder
        .select(Projections.constructor(TaskCountResponse.class,
            createTotalCountExpression().as("totalCount"),
            createAssignedCountExpression().as("assignedCount"),
            createIncompleteCountExpression().as("incompleteCount"),
            createCompleteCountExpression().as("completedCount"),
            createOverdueCountExpression().as("overdueCount")))
        .from(task)
        .where(task.project.workspace.id.eq(workspaceId).and(task.assignee.id.eq(userId)))
        .fetchOne();
  }

  @Override
  public TaskCountResponse taskCountsMonthlyByWorkspaceId(UUID workspaceId, LocalDateTime start,
      LocalDateTime end) {
    return queryBuilder
        .select(Projections.constructor(TaskCountResponse.class,
            createTotalCountExpression().as("totalCount"),
            createAssignedCountExpression().as("assignedCount"),
            createIncompleteCountExpression().as("incompleteCount"),
            createCompleteCountExpression().as("completedCount"),
            createInProgressCountExpression().as("inProgressCount"),
            createTodoCountExpression().as("todoCount"),
            createOverdueCountExpression().as("overdueCount"),
            createInReviewCountExpression().as("inReviewCount"),
            createBacklogCountExpression().as("backlogCount")))
        .from(task)
        .where(task.project.workspace.id.eq(workspaceId)
            .and(task.createdAt.between(start, end)))
        .fetchOne();
  }

  @Override
  public TaskCountResponse taskCountsTotalByWorkspaceId(UUID workspaceId) {
    return queryBuilder
        .select(Projections.fields(TaskCountResponse.class,
            createTotalCountExpression().as("totalCount"),
            createAssignedCountExpression().as("assignedCount"),
            createIncompleteCountExpression().as("incompleteCount"),
            createCompleteCountExpression().as("completedCount"),
            createInProgressCountExpression().as("inProgressCount"),
            createTodoCountExpression().as("todoCount"),
            createOverdueCountExpression().as("overdueCount"),
            createInReviewCountExpression().as("inReviewCount"),
            createBacklogCountExpression().as("backlogCount")))
        .from(task)
        .where(task.project.workspace.id.eq(workspaceId))
        .fetchOne();
  }

  private NumberExpression<Long> createTodoCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} = {1} then 1 else 0 end), 0)",
        task.status, TaskStatus.TODO);
  }

  private NumberExpression<Long> createInProgressCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} = {1} then 1 else 0 end), 0)",
        task.status, TaskStatus.IN_PROGRESS);
  }

  private NumberExpression<Long> createInReviewCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} = {1} then 1 else 0 end), 0)",
        task.status, TaskStatus.IN_REVIEW);
  }

  private NumberExpression<Long> createBacklogCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} = {1} then 1 else 0 end), 0)",
        task.status, TaskStatus.BACKLOG);
  }

  private NumberExpression<Long> createTotalCountExpression() {
    return Expressions.numberTemplate(Long.class, "coalesce(count(distinct {0}), 0)", task.id);
  }

  private NumberExpression<Long> createAssignedCountExpression() {
    return Expressions.numberTemplate(Long.class, "coalesce(sum(case when {0} is not null then 1 else 0 end), 0)",
        task.assignee);
  }

  private NumberExpression<Long> createIncompleteCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} <> {1} then 1 else 0 end), 0)",
        task.status, TaskStatus.COMPLETED);
  }

  private NumberExpression<Long> createCompleteCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} = {1} then 1 else 0 end), 0)",
        task.status, TaskStatus.COMPLETED);
  }

  private NumberExpression<Long> createOverdueCountExpression() {
    return Expressions.numberTemplate(Long.class,
        "coalesce(sum(case when {0} < {1} and {2} <> {3} then 1 else 0 end), 0)",
        task.dueDate, CURRENT_DATE, task.status, TaskStatus.COMPLETED);
  }

}
