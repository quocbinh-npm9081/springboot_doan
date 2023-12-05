package vn.eztek.springboot3starter.profile.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class GetMyProfileQueryValidated implements QueryValidated {

  User user;

}
