package vn.eztek.springboot3starter.project.command.event;

import lombok.Value;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class ProjectCreatedEvent implements Event {

  ProjectAggregateId id;
  String userId;
  String name;
  String description;

}
