package vn.eztek.springboot3starter.task.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CreateTaskCommandValidated implements CommandValidated {

  User owner;
  Stage stage;
  Project project;
  Task previous;
  Task next;
}
