package vn.eztek.springboot3starter.notification.command.validated;

import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.domain.notification.entity.Notification;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;


@Value(staticConstructor = "validatedOf")
public class MarkAsReadNotificationCommandValidated implements CommandValidated {

  List<Notification> notification;
  User user;

}
