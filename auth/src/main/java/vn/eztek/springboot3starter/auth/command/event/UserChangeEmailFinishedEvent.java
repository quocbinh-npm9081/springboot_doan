package vn.eztek.springboot3starter.auth.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class UserChangeEmailFinishedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  AuthAggregateId id;
  UserStatus status;
  String userId;
}
