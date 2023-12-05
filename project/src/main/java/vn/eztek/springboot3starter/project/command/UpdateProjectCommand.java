package vn.eztek.springboot3starter.project.command;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateProjectCommand implements Command {

  UUID id;
  String name;
  String description;
  ProjectStatus status;

}
