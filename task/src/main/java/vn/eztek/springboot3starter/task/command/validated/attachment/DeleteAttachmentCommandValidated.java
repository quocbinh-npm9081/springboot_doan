package vn.eztek.springboot3starter.task.command.validated.attachment;

import lombok.Value;
import vn.eztek.springboot3starter.domain.attachment.entity.Attachment;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class DeleteAttachmentCommandValidated implements CommandValidated {

  Task task;
  Attachment attachment;
  User logginUser;
}
