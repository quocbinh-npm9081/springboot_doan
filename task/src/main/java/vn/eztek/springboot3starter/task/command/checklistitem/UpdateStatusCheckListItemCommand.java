package vn.eztek.springboot3starter.task.command.checklistitem;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateStatusCheckListItemCommand implements Command {

  UUID taskId;
  UUID checkListId;
  UUID id;
  Boolean status;
}
