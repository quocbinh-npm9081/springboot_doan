package vn.eztek.springboot3starter.user.command;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateUserStatusCommand implements Command {

  UUID userId;
  UserStatus status;
}
