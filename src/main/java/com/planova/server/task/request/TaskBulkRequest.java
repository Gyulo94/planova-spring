package com.planova.server.task.request;

import java.util.UUID;

import com.planova.server.task.entity.TaskStatus;

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
public class TaskBulkRequest {
  private UUID id;
  private TaskStatus status;
  private int position;

}
