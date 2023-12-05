package vn.eztek.springboot3starter.user.command.validated;

import java.util.Set;
import lombok.Value;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CreateUserCommandValidated implements CommandValidated {

  Role role;
  Set<Privilege> privileges;

}
