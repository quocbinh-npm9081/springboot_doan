package vn.eztek.springboot3starter.task.command.validated.label;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CreateLabelCommandValidated implements CommandValidated {
  User loggedInUser;
}
