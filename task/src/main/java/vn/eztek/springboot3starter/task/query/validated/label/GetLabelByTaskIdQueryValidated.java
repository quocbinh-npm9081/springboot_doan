package vn.eztek.springboot3starter.task.query.validated.label;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class GetLabelByTaskIdQueryValidated implements QueryValidated {
}
