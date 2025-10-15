package com.planova.server.task.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.planova.server.project.entity.Project;
import com.planova.server.task.request.TaskBulkRequest;
import com.planova.server.task.request.TaskRequest;
import com.planova.server.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
public class Task {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String name;

  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TaskStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority")
  private TaskPriority priority;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "due_date")
  private LocalDateTime dueDate;

  @Min(1000)
  @Max(1000000)
  private int position;

  public void update(TaskRequest request, User assignee) {
    this.name = request.getName();
    this.description = request.getDescription();
    this.status = request.getStatus();
    this.priority = request.getPriority();
    this.startDate = request.getStartDate();
    this.dueDate = request.getDueDate();
    this.assignee = assignee;
  }

  public void updateStatusOrPosition(TaskBulkRequest request) {
    this.status = request.getStatus();
    this.position = request.getPosition();
  }
}
