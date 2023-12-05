package vn.eztek.springboot3starter.task.query;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class GetTaskByIdQuery implements Query {

  UUID id;
}
