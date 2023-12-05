package vn.eztek.springboot3starter.project.query.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class GetProjectByIdQueryValidated implements QueryValidated {

  Project project;

}
