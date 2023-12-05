package vn.eztek.springboot3starter.project.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class ProjectDeletedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;

  ProjectAggregateId id;
  String userId;
  String projectId;
  String name;
  String description;

}
