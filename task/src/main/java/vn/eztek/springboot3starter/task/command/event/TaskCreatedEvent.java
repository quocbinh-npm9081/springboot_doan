package vn.eztek.springboot3starter.task.command.event;

import java.io.Serial;
import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Value(staticConstructor = "eventOf")
public class TaskCreatedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  TaskAggregateId id;
  String userId;
  String taskId;
  String title;
  String description;
  UUID projectId;
  UUID stageId;
  UUID previousId;
  UUID nextId;
}
