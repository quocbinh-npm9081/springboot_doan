package vn.eztek.springboot3starter.project.command;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class DeleteProjectCommand implements Command {

  UUID id;
}
