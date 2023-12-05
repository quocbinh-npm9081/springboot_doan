package vn.eztek.springboot3starter.invitation.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class ListInvitationQueryValidated implements QueryValidated {

  User user;
}
