package vn.eztek.springboot3starter.task.command;

import com.github.fge.jsonpatch.JsonPatch;
import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateTaskCommand implements Command {

  UUID taskId;
  JsonPatch input;

}
