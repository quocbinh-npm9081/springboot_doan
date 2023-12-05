package vn.eztek.springboot3starter.publicAccess.query;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class GetProjectByKeyQuery implements Query {

  String key;
}
