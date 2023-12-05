package vn.eztek.springboot3starter.auth.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class ActivateAccountFinishedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;

  AuthAggregateId id;
  String userId;
}
