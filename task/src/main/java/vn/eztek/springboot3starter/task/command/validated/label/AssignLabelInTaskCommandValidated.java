package vn.eztek.springboot3starter.task.command.validated.label;

import lombok.Value;
import vn.eztek.springboot3starter.domain.label.entity.Label;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

import java.util.List;

@Value(staticConstructor = "validatedOf")
public class AssignLabelInTaskCommandValidated implements CommandValidated {
  List<Label> labels;
  User loggedInUser;
}
