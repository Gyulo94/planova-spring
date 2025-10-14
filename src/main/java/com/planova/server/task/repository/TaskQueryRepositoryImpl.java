package com.planova.server.task.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.planova.server.task.entity.QTask;
import com.planova.server.task.entity.Task;
import com.planova.server.task.request.TaskFilterRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

  private final JPAQueryFactory queryBuilder;

  @Override
  public List<Task> findByProjectIdAndFilters(UUID projectId, TaskFilterRequest request) {
    QTask task = QTask.task;
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
}
