package vn.eztek.springboot3starter.publicAccess.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class GetProjectByKeyQueryValidated implements QueryValidated {

  Invitation invitation;
  User user;

}
