package vn.eztek.springboot3starter.project.command.handler;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.NotificationSocketMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.AddMembersToProjectCommand;
import vn.eztek.springboot3starter.project.command.event.MembersAddedToProjectEvent;
import vn.eztek.springboot3starter.project.command.validator.AddMembersToProjectCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class AddMembersToProjectCommandHandler implements
    CommandHandler<AddMembersToProjectCommand, EmptyCommandResult, ProjectAggregateId> {

  private final AddMembersToProjectCommandValidator validator;
  private final ProjectEventService eventStoreService;
  private final SendGridProperties sendGridProperties;
  private final UserProjectRepository userProjectRepository;
  private final ProjectMapper projectMapper;
  private final RedisMessagePublisher redisMessagePublisher;

  @Override
  public EmptyCommandResult handle(AddMembersToProjectCommand command,
      ProjectAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    List<UserProject> userProjects = new ArrayList<>();
    for (var user : validated.getUsers()) {
      userProjects.add(projectMapper.mapToUserProjectBeforeCreate(user, validated.getProject(),
          UserProjectStatus.JOINED));
    }
    userProjectRepository.saveAll(userProjects);

    // storing event
    var event = MembersAddedToProjectEvent.eventOf(entityId,
        validated.getLoggedInUser().getId().toString(), validated.getProject().getId().toString(),
        command.getEmails());
    eventStoreService.store(event);

    //notification
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    var projectId = jsonNodeFactory.textNode(validated.getProject().getId().toString());
    var metadata = jsonNodeFactory.objectNode();
    metadata.set("projectId", projectId);
    for (var user : validated.getUsers()) {
      redisMessagePublisher.publish(
          NotificationSocketMessage.create(SocketResponseType.PROJECT_INVITED, metadata,
              SocketResponseType.PROJECT_INVITED, user.getId()));
    }
    // send message to queue
    sendMessage(validated.getUsers(), validated.getProject(), validated.getLoggedInUser());
    // resulting
    return EmptyCommandResult.empty();
  }

  private void sendMessage(List<User> users, Project project, User loggedInUser) {
    for (var user : users) {
      var templateData = new HashMap<String, String>();
      templateData.put("agencyName",
          loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
      templateData.put("memberName", user.getFirstName() + " " + user.getLastName());
      templateData.put("projectName", project.getName());
      templateData.put("currentDate",
          DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
      templateData.put("email", user.getUsername());

      redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
          sendGridProperties.getDynamicTemplateId().getInviteMember(), templateData));
    }
  }
}
