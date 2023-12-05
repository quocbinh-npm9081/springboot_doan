package vn.eztek.springboot3starter.profile.query;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class GetMyProfileQuery implements Query {

  String username;


}
