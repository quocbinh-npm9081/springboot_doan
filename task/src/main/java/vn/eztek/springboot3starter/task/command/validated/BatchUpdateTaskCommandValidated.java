package vn.eztek.springboot3starter.task.command.validated;

import java.util.Map;
import lombok.Value;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class BatchUpdateTaskCommandValidated implements CommandValidated {

  User loggedInUser;
  Map<Task, Stage> tasks;

}
