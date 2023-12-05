package vn.eztek.springboot3starter.project.command.event;

import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class TalentInviteEvent implements Event {

  ProjectAggregateId id;
  String userId;
  List<String> emails;

}
