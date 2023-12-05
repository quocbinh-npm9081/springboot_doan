package vn.eztek.springboot3starter.task.command.validated.checklist;

import lombok.Value;
import vn.eztek.springboot3starter.domain.checkList.entity.CheckList;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class UpdateCheckListCommandValidated implements CommandValidated {

  User loggedInUser;
  CheckList checkList;
}

