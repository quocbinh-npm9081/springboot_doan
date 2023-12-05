package vn.eztek.springboot3starter.user.command.event;

import java.io.Serial;
import java.util.Set;
import lombok.Value;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.shared.cqrs.Event;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Value(staticConstructor = "eventOf")
public class UserCreatedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  UserAggregateId id;
  String userId;
  String firstName;
  String lastName;
  String username;
  Gender gender;
  String phoneNumber;
  RoleName role;
  Set<PrivilegeName> privileges;

}
