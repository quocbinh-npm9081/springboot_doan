package vn.eztek.springboot3starter.user.query;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class GetUserByIdQuery implements Query {

  UUID id;

}
