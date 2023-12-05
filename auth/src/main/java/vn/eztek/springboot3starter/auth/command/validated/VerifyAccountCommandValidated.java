package vn.eztek.springboot3starter.auth.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.key.entity.Key;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class VerifyAccountCommandValidated implements CommandValidated {

  User user;
  Key key;
}
