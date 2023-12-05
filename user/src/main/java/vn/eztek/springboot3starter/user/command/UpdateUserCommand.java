package vn.eztek.springboot3starter.user.command;

import java.util.Set;
import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateUserCommand implements Command {

  UUID userId;
  String firstName;
  String lastName;
  String phoneNumber;
  Gender gender;
  RoleName role;
  Set<PrivilegeName> privileges;

}
