package vn.eztek.springboot3starter.publicAccess.mapper;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;
import vn.eztek.springboot3starter.domain.key.entity.Key;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.publicAccess.command.InvitationMoveOnCommand;
import vn.eztek.springboot3starter.publicAccess.command.InvitationViaLinkCommand;
import vn.eztek.springboot3starter.publicAccess.response.ProjectResponse;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PublicAccessMapper {

  @Value("${key.expirationVerifyAccountMs}")
  private Long expirationMs;

  @Mappings({@Mapping(target = "firstName", source = "command.firstName"),
      @Mapping(target = "lastName", source = "command.lastName"),
      @Mapping(target = "phoneNumber", source = "command.phoneNumber"),
      @Mapping(target = "gender", source = "command.gender"),
      @Mapping(target = "status", source = "status"),
      @Mapping(target = "password", source = "password")})
  public abstract User mapToUserBeforeMovingOnInvitation(User user, InvitationMoveOnCommand command,
      String password, UserStatus status);

  @Mappings({@Mapping(target = "firstName", source = "command.firstName"),
      @Mapping(target = "lastName", source = "command.lastName"),
      @Mapping(target = "phoneNumber", source = "command.phoneNumber"),
      @Mapping(target = "gender", source = "command.gender"),
      @Mapping(target = "username", source = "command.username"),
      @Mapping(target = "status", source = "status"),
      @Mapping(target = "role", source = "role"),
      @Mapping(target = "privileges", source = "privileges"),
      @Mapping(target = "password", source = "password")})
  public abstract User mapToUserBeforeCreateAndJoin(InvitationViaLinkCommand command, Role role,
      Set<Privilege> privileges, String password, UserStatus status);

  @Mappings({@Mapping(target = "id", ignore = true),
      @Mapping(target = "status", source = "projectStatus"),})
  public abstract UserProject mapToUserProjectBeforeCreate(User user, Project project,
      UserProjectStatus projectStatus);

  @Mappings({@Mapping(target = "user", source = "user"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),
      @Mapping(target = "action", source = "keyType")})
  public abstract Key mapToKey(User user, String key, KeyType keyType);

  public abstract ProjectResponse mapToProjectResponse(Project project);

  public ZonedDateTime getExpiredTime() {
    return ZonedDateTime.now().plus(expirationMs, ChronoUnit.MILLIS);
  }
}
