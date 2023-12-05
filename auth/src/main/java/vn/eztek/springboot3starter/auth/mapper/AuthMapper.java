package vn.eztek.springboot3starter.auth.mapper;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;
import vn.eztek.springboot3starter.auth.response.CheckUserResponse;
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.key.entity.Key;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AuthMapper {

  @Value("${key.expirationMs}")
  private Long expirationMs;

  @Mappings({@Mapping(target = "user", source = "user"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),
      @Mapping(target = "action", source = "keyType")})
  public abstract Key mapToKey(User user, String key, KeyType keyType);

  @Mapping(target = "username", source = "email")
  @Mapping(target = "status", source = "status")
  public abstract User mapToUserBeforeChangeEmail(User user, String email, UserStatus status);

  @Mappings({@Mapping(target = "status", source = "status"),})
  public abstract User mapToUserBeforeUpdateStatus(User user, UserStatus status);

  @Mapping(target = "password", source = "newPassword")
  public abstract User mapToUserBeforeResetPassword(@MappingTarget User user, String newPassword);

  public ZonedDateTime getExpiredTime() {
    return ZonedDateTime.now().plus(expirationMs, ChronoUnit.MILLIS);
  }

  public abstract CheckUserResponse mapToCheckUserResponse(Boolean isRegisteringUser);

  @Mapping(target = "deletedAt", source = "deletedAt")
  public abstract User mapToUserBeforeUpdateDeleteAt(User project, ZonedDateTime deletedAt);

  @Mappings({

      @Mapping(target = "userId", source = "userProject.user.id"),
      @Mapping(target = "projectId", source = "userProject.project.id"),
      @Mapping(target = "projectName", source = "userProject.project.name")})
  public abstract UserProjectMessage mapToUserProjectMessage(UserProject userProject);

}
