package vn.eztek.springboot3starter.user.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.user.query.ListUsersQuery;
import vn.eztek.springboot3starter.user.query.validated.ListUsersQueryValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ListUsersQueryValidator extends
    QueryValidation<ListUsersQuery, ListUsersQueryValidated> {

  @Override
  public ListUsersQueryValidated validate(ListUsersQuery query) {
    return ListUsersQueryValidated.validatedOf();
  }

}
