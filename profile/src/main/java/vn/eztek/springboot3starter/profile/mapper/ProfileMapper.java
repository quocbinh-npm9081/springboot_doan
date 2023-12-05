package vn.eztek.springboot3starter.profile.mapper;

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
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.key.entity.Key;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.profile.command.UpdateProfileCommand;
import vn.eztek.springboot3starter.profile.response.ProfileResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProfileMapper {

  @Value("${key.expirationMs}")
  private Integer expirationMs;

  @Mappings({
      @Mapping(target = "role", expression = "java(user.getRole().getName())"),
      @Mapping(target = "privileges", expression = "java(getPrivilegeNames(user.getPrivileges()))"),
      @Mapping(target = "lastSignedInTime", source = "lastSignedInTime"),
  })
  public abstract ProfileResponse mapToProfileResponse(User user, ZonedDateTime lastSignedInTime);

  @Mappings({
      @Mapping(target = "user", source = "user"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),
      @Mapping(target = "action", source = "keyType")
  })
  public Set<PrivilegeName> getPrivilegeNames(Set<Privilege> privileges) {
    return privileges.stream().map(Privilege::getName).collect(Collectors.toSet());
  }


  @Mappings({
      @Mapping(target = "user", source = "user"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),
      @Mapping(target = "action", source = "keyType"),


  })
  public abstract Key mapToKey(User user, String key, KeyType keyType);

  public abstract User mapToUserBeforeUpdate(@MappingTarget User user,
      UpdateProfileCommand command);

  @Mappings({

      @Mapping(target = "role", expression = "java(user.getRole().getName())"),
      @Mapping(target = "privileges", expression = "java(getPrivilegeNames(user.getPrivileges()))"),
      @Mapping(target = "lastSignedInTime", source = "lastSignedInTime")
  })
  public abstract ProfileResponse mapToUserResponse(User user, ZonedDateTime lastSignedInTime);

  @Mappings({

      @Mapping(target = "userId", source = "userProject.user.id"),
      @Mapping(target = "projectId", source = "userProject.project.id"),
      @Mapping(target = "projectName", source = "userProject.project.name")
  })
  public abstract UserProjectMessage mapToUserProjectMessage(UserProject userProject);

  public ZonedDateTime getExpiredTime() {
    return ZonedDateTime.now().plus(expirationMs, ChronoUnit.MILLIS);
  }
}
