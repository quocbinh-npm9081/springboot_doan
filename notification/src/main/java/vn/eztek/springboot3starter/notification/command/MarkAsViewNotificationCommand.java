package vn.eztek.springboot3starter.notification.command;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class MarkAsViewNotificationCommand implements Command {

}
