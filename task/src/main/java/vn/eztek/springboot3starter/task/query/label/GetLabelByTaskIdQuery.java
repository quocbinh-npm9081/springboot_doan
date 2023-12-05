package vn.eztek.springboot3starter.task.query.label;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

import java.util.UUID;

@Value(staticConstructor = "queryOf")

public class GetLabelByTaskIdQuery implements Query {
  UUID taskId;
}
