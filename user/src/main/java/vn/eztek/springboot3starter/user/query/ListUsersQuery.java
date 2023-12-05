package vn.eztek.springboot3starter.user.query;

import lombok.Value;
import org.springframework.data.domain.Pageable;
import vn.eztek.springboot3starter.shared.cqrs.Query;
import vn.eztek.springboot3starter.user.specification.UserCriteria;

@Value(staticConstructor = "queryOf")
public class ListUsersQuery implements Query {

  UserCriteria criteria;
  Pageable pageable;

}
