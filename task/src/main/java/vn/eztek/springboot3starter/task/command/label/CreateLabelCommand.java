package vn.eztek.springboot3starter.task.command.label;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

import java.util.UUID;

@Value(staticConstructor = "commandOf")
public class CreateLabelCommand implements Command {

  UUID taskId;
  String name;
  String color;
}