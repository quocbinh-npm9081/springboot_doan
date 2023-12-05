package vn.eztek.springboot3starter.invitation.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class ConfirmInviteCommandValidated implements CommandValidated {

  Invitation invitation;
  User loggedInUser;
  Project project;
  Boolean isMember;
}
