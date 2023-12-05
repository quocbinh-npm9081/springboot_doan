package vn.eztek.springboot3starter.project.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class ProjectResponse implements CommandResult, QueryResult {

  UUID id;
  String name;
  String description;
  ProjectStatus status;

  @JsonInclude(Include.NON_NULL)
  UserProjectStatus userProjectStatus;

  @JsonInclude(Include.NON_NULL)
  ZonedDateTime deletedAt;

}
