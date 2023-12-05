package vn.eztek.springboot3starter.user.query;

import lombok.Value;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class AutoCompleteSearchQuery implements Query {

  String name;
  RoleName roleName;
}
