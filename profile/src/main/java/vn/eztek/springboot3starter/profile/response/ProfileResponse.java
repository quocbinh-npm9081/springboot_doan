package vn.eztek.springboot3starter.profile.response;

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
public class ProfileResponse implements CommandResult, QueryResult {

  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private RoleName role;
  private String phoneNumber;
  private Gender gender;
  private Set<PrivilegeName> privileges;
  private UserStatus status;
  private ZonedDateTime lastSignedInTime;
}
