package vn.eztek.springboot3starter.task.query.checklist;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class GetCheckListByTaskIdQuery implements Query {

  UUID taskId;
}
