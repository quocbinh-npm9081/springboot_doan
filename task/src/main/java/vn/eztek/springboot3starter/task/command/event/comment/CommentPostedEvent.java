package vn.eztek.springboot3starter.task.command.event.comment;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Value(staticConstructor = "eventOf")
public class CommentPostedEvent implements Event {

  TaskAggregateId id;
  String userId;
  String taskId;
  String content;

}
