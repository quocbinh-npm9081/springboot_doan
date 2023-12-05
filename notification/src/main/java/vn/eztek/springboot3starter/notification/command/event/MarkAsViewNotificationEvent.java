package vn.eztek.springboot3starter.notification.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.notification.vo.NotificationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class MarkAsViewNotificationEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;

  NotificationAggregateId id;
  String userId;
}
