package vn.eztek.springboot3starter.publicAccess.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class InvitationMoveOnCommandValidated implements CommandValidated {

  Invitation invitation;
  User user;

}
