package vn.eztek.springboot3starter.task.command.validated.checklist;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CreateCheckListCommandValidated implements CommandValidated {

  User loggedInUser;
}

