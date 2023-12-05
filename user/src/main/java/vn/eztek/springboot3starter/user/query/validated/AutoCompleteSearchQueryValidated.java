package vn.eztek.springboot3starter.user.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class AutoCompleteSearchQueryValidated implements QueryValidated {

  User loggedInUser;
  Role role;
}
