package vn.eztek.springboot3starter.task.command.event.label;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

import java.io.Serial;

@Value(staticConstructor = "eventOf")
public class LabelRemovedColorEvent implements Event {
  @Serial
  private static final long serialVersionUID = 1L;
  TaskAggregateId id;
  String userId;
  String labelId;
  String color;
}
