package vn.eztek.springboot3starter.auth.command.validated;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CheckRegisteringUserCommandValidated implements CommandValidated {

  Boolean isRegisteringUser;
  UUID userId;
}
