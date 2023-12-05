package vn.eztek.springboot3starter.task.command.event.attachment;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Value(staticConstructor = "eventOf")
public class AttachmentUploadedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  TaskAggregateId id;
  String userId;
  String taskId;
  String attachmentId;
  String originalName;
  String name;
}
