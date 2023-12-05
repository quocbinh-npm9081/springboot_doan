package vn.eztek.springboot3starter.invitation.command;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class ResponseInviteCommand implements Command {

  UUID id;
  Boolean accept;
}
