package vn.eztek.springboot3starter.user.specification;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.user.annotation.RoleNamesAllowed;

@Getter
@Setter
public class UserCriteria {

  @RoleNamesAllowed(values = {RoleName.AGENCY, RoleName.PROJECT_MANAGER, RoleName.TALENT})
  List<RoleName> roles;
  private List<UserStatus> statuses;
  private String keyword;

}
