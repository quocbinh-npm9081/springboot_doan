package vn.eztek.springboot3starter.user.mapper;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;
import vn.eztek.springboot3starter.domain.key.entity.Key;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.user.command.CreateUserCommand;
import vn.eztek.springboot3starter.user.command.UpdateUserCommand;
import vn.eztek.springboot3starter.user.response.AutoCompleteSearchResponse;
import vn.eztek.springboot3starter.user.response.UserResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

  @Value("${key.expirationMs}")
  private Integer expirationMs;

  @Mappings({
      @Mapping(target = "role", expression = "java(user.getRole().getName())"),
      @Mapping(target = "privileges", expression = "java(getPrivilegeNames(user.getPrivileges()))"),
      @Mapping(target = "lastSignedInTime", source = "lastSignedInTime")
  })
  public abstract UserResponse mapToUserResponse(User user, ZonedDateTime lastSignedInTime);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "password", source = "password"),
      @Mapping(target = "role", source = "role"),
      @Mapping(target = "privileges", source = "privileges"),
      @Mapping(target = "status", expression = "java(getUserStatusInActive())")
  })
  public abstract User mapToUserBeforeCreate(CreateUserCommand command, String password,
      boolean isVerified, Role role,
      Set<Privilege> privileges);

  @Mappings({
      @Mapping(target = "id", source = "command.userId"),
      @Mapping(target = "role", source = "role"),
      @Mapping(target = "privileges", source = "privileges")
  })
  public abstract User mapToUserBeforeUpdate(@MappingTarget User user, UpdateUserCommand command,
      Role role, Set<Privilege> privileges);

  @Mappings({
      @Mapping(target = "user", source = "user"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),
      @Mapping(target = "action", source = "keyType")
  })
  public abstract Key mapToKey(User user, String key, KeyType keyType);

  public abstract AutoCompleteSearchResponse mapToAutoCompleteSearch(User user);

  @Mappings({
      @Mapping(target = "status", source = "status"),
  })
  public abstract User mapToUserBeforeUpdateStatus(User user, UserStatus status);

  public UserStatus getUserStatusInActive() {
    return UserStatus.INACTIVE;
  }

  public ZonedDateTime getExpiredTime() {
    return ZonedDateTime.now().plus(expirationMs, ChronoUnit.MILLIS);
  }

  public Set<PrivilegeName> getPrivilegeNames(Set<Privilege> privileges) {
    return privileges.stream().map(Privilege::getName).collect(Collectors.toSet());
  }

}
