package vn.eztek.springboot3starter.project.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class ListProjectQueryValidated implements QueryValidated {

}
