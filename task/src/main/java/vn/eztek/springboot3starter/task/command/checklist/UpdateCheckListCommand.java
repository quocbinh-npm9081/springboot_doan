package vn.eztek.springboot3starter.task.command.checklist;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateCheckListCommand implements Command {

  UUID taskId;
  UUID id;
  String name;
}
