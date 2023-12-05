package vn.eztek.springboot3starter.project.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class DeleteSharedLinkCommandValidated implements CommandValidated {

  User user;
  Invitation invitation;

}
