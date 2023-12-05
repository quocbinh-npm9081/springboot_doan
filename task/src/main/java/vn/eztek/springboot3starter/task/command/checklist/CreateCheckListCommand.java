package vn.eztek.springboot3starter.task.command.checklist;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class CreateCheckListCommand implements Command {

  UUID taskId;
  String name;
}
