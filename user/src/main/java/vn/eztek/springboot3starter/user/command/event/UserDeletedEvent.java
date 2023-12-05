package vn.eztek.springboot3starter.user.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Value(staticConstructor = "eventOf")
public class UserDeletedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  UserAggregateId id;
  String userId;

}
