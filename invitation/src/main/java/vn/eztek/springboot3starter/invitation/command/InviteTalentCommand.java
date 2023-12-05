package vn.eztek.springboot3starter.invitation.command;

import java.util.List;
import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class InviteTalentCommand implements Command {

  UUID projectId;
  List<String> emails;
}
