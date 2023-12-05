package vn.eztek.springboot3starter.task.command;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class AssignTaskCommand implements Command {

  UUID taskId;
  UUID userId;
}
