package vn.eztek.springboot3starter.user.command;

import java.util.Set;
import lombok.Value;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class CreateUserCommand implements Command {

  String firstName;
  String lastName;
  String username;
  String phoneNumber;
  Gender gender;
  String password;
  RoleName role;
  Set<PrivilegeName> privileges;

}
