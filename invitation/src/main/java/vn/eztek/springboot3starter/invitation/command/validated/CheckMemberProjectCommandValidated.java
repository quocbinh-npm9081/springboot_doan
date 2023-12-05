package vn.eztek.springboot3starter.invitation.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CheckMemberProjectCommandValidated implements CommandValidated {

  User user;
  Invitation invitation;
  UserProject userProject;
}
