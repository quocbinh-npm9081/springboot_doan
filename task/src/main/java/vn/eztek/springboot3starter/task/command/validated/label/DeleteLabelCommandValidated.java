package vn.eztek.springboot3starter.task.command.validated.label;

import lombok.Value;
import vn.eztek.springboot3starter.domain.label.entity.Label;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class DeleteLabelCommandValidated implements CommandValidated {
  User loggedInUser;
  Label label;
}
