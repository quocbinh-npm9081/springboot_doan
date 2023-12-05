package vn.eztek.springboot3starter.invitation.command;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class ConfirmInviteCommand implements Command {

  String key;
}
