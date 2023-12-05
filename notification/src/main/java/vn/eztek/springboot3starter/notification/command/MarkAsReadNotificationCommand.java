package vn.eztek.springboot3starter.notification.command;

import java.util.List;
import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;


@Value(staticConstructor = "commandOf")
public class MarkAsReadNotificationCommand implements Command {

  List<UUID> notificationIds;

}
