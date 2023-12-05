package vn.eztek.springboot3starter.user.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class ListUsersQueryValidated implements QueryValidated {

}
