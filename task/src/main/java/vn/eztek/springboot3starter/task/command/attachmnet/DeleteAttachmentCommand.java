package vn.eztek.springboot3starter.task.command.attachmnet;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class DeleteAttachmentCommand implements Command {

  UUID taskId;
  UUID attachmentId;
}
