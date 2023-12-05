package vn.eztek.springboot3starter.profile.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class ProfileDeactivatedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  ProfileAggregateId id;
  String userId;

}
