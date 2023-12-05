package vn.eztek.springboot3starter.task.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class UpdateTaskCommandValidated implements CommandValidated {

  User loggedInUser;
  Task task;

}
