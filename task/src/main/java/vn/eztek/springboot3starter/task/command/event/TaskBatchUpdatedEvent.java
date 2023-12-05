package vn.eztek.springboot3starter.task.command.event;

import java.io.Serial;
import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.task.request.BatchUpdateTaskRequest;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Value(staticConstructor = "eventOf")
public class TaskBatchUpdatedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  TaskAggregateId id;
  String userId;
  List<BatchUpdateTaskRequest> taskUpdates;

}
