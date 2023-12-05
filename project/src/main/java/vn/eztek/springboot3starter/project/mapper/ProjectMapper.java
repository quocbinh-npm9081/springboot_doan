package vn.eztek.springboot3starter.project.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.notification.entity.Notification;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.project.command.CreateProjectCommand;
import vn.eztek.springboot3starter.project.command.UpdateProjectCommand;
import vn.eztek.springboot3starter.project.response.LinkShareResponse;
import vn.eztek.springboot3starter.project.response.ProjectMemberResponse;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.response.StageResponse;
import vn.eztek.springboot3starter.shared.socket.NotificationResponse;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.StageSocketData;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public abstract class ProjectMapper {

  @Value("${key.invitationMs}")
  private Long invitationMs;

  @Mappings({@Mapping(target = "status", source = "project.status"),
      @Mapping(target = "name", source = "project.name"),
      @Mapping(target = "description", source = "project.description"),
      @Mapping(target = "deletedAt", source = "project.deletedAt"),})
  public abstract ProjectResponse mapToProjectResponse(Project project, UserProjectStatus status);

  @Mappings({@Mapping(target = "id", ignore = true),
      @Mapping(target = "status", source = "status"),})
  public abstract Project mapToBeforeCreate(CreateProjectCommand command, UUID ownerId,
      ProjectStatus status);

  @Mappings({@Mapping(target = "id", source = "command.id"),
      @Mapping(target = "name", source = "command.name"),
      @Mapping(target = "description", source = "command.description"),
      @Mapping(target = "status", source = "command.status"),})
  public abstract Project mapToProjectBeforeUpdate(Project project, UpdateProjectCommand command);

  @Mappings({@Mapping(target = "id", source = "projectUser.user.id"),
      @Mapping(target = "email", source = "projectUser.user.username"),
      @Mapping(target = "phoneNumber", source = "projectUser.user.phoneNumber"),
      @Mapping(target = "status", source = "projectUser.status"),
      @Mapping(target = "firstName", source = "projectUser.user.firstName"),
      @Mapping(target = "lastName", source = "projectUser.user.lastName"),

  })
  public abstract ProjectMemberResponse mapToProjectMember(UserProject projectUser);


  @Mappings({@Mapping(target = "id", ignore = true),
      @Mapping(target = "status", source = "projectStatus"),})
  public abstract UserProject mapToUserProjectBeforeCreate(User user, Project project,
      UserProjectStatus projectStatus);

  public ZonedDateTime getExpiredTime() {
    return ZonedDateTime.now().plus(invitationMs, ChronoUnit.MILLIS);
  }

  @Mappings({@Mapping(target = "orderNumber", source = "orderNumber"),})
  public abstract Stage mapToStage(Stage stage, Integer orderNumber);

  public abstract StageSocketData mapToStageSocketData(Stage stage);

  public abstract StageResponse mapToStageResponse(Stage stage);

  @Mapping(target = "deletedAt", source = "deletedAt")
  public abstract Project mapToProjectBeforeUpdateDeleteAt(Project project,
      ZonedDateTime deletedAt);


  public abstract Notification mapBeforeCreateNotification(
      SocketResponseType notificationType, JsonNode metadata);

  @Mappings({

      @Mapping(target = "userId", source = "userProject.user.id"),
      @Mapping(target = "projectId", source = "userProject.project.id"),
      @Mapping(target = "projectName", source = "userProject.project.name")})
  public abstract UserProjectMessage mapToUserProjectMessage(UserProject userProject);

  @Mappings({@Mapping(target = "status", source = "status"),})
  public abstract UserProject mapToUserProjectBeforeUpdate(UserProject userProject,
      UserProjectStatus status);

  @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "createdBy", ignore = true),
      @Mapping(target = "createdDate", ignore = true),
      @Mapping(target = "lastModifiedBy", ignore = true),
      @Mapping(target = "lastModifiedDate", ignore = true),
      @Mapping(target = "user", source = "user"),
      @Mapping(target = "inviter", source = "inviter"),
      @Mapping(target = "project", source = "project"),
      @Mapping(target = "action", source = "action"),
      @Mapping(target = "used", expression = "java(false)"),
      @Mapping(target = "expiredTime", expression = "java(getExpiredTime())"),})
  public abstract Invitation mapToInvitation(User user, User inviter, Project project, String key,
      InvitationType action);

  public abstract LinkShareResponse mapToLinkShareResponse(String key);
}
