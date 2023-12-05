package vn.eztek.springboot3starter.user.response;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class UserResponse implements CommandResult, QueryResult {

  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private Gender gender;
  private String phoneNumber;
  private RoleName role;
  private Set<PrivilegeName> privileges;
  private UserStatus status;
  private ZonedDateTime lastSignedInTime;

}
