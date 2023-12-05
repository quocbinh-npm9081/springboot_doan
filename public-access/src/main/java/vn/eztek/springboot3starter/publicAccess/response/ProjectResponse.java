package vn.eztek.springboot3starter.publicAccess.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class ProjectResponse implements QueryResult {

  UUID id;
  String name;
  String description;
  ProjectStatus status;
}
