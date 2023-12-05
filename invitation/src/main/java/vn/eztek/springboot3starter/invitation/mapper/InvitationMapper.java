package vn.eztek.springboot3starter.invitation.mapper;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.invitation.response.CheckUserMatchInviteResponse;
import vn.eztek.springboot3starter.invitation.response.InvitationResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class InvitationMapper {

  @Value("${key.invitationMs}")
  private Long invitationMs;

  @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "createdBy", ignore = true),
      @Mapping(target = "createdDate", ignore = true),
      @Mapping(target = "lastModifiedBy", ignore = true),
      @Mapping(target = "lastModifiedDate", ignore = true),
      @Mapping(target = "user", source = "user"), @Mapping(target = "inviter", source = "inviter"),
      @Mapping(target = "project", source = "project"),
      @Mapping(target = "action", source = "action"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),})
  public abstract Invitation mapToInvitation(User user, User inviter, Project project, String key,
      InvitationType action);

  @Mappings({@Mapping(target = "id", ignore = true),
      @Mapping(target = "status", source = "status"),})
  public abstract UserProject mapToUserProjectBeforeCreate(User user, Project project,
      UserProjectStatus status);

  @Mappings({@Mapping(target = "projectName", source = "invitation.project.name"),
      @Mapping(target = "inviter.email", source = "invitation.inviter.username"),
      @Mapping(target = "inviter.firstName", source = "invitation.inviter.firstName"),
      @Mapping(target = "inviter.lastName", source = "invitation.inviter.lastName"),})
  public abstract InvitationResponse mapToInvitationResponse(Invitation invitation);

  public ZonedDateTime getExpiredTime() {
    return ZonedDateTime.now().plus(invitationMs, ChronoUnit.MILLIS);
  }

  public abstract CheckUserMatchInviteResponse mapToCheckResponse(Boolean isMatch);
}
