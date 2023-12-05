package vn.eztek.springboot3starter.project.query.validated;

import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class ListProjectMembersQueryValidated implements QueryValidated {

  List<UserProject> userProjects;

}
