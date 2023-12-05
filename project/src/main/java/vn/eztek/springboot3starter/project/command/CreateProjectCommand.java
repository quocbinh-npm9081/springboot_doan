package vn.eztek.springboot3starter.project.command;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class CreateProjectCommand implements Command {

  String name;
  String description;

}
