package vn.eztek.springboot3starter.task.command.comment;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateCommentCommand implements Command {

  UUID taskId;
  UUID commentId;
  String content;
}
