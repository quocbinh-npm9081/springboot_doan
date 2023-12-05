package vn.eztek.springboot3starter.project.command.handler;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.UpdateStatusMemberInProjectCommand;
import vn.eztek.springboot3starter.project.command.event.StatusMemberUpdatedInProjectEvent;
import vn.eztek.springboot3starter.project.command.validator.UpdateStatusMemberInProjectCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class UpdateStatusMemberInProjectCommandHandler implements
    CommandHandler<UpdateStatusMemberInProjectCommand, EmptyCommandResult, ProjectAggregateId> {

  private final UpdateStatusMemberInProjectCommandValidator validator;
  private final ProjectEventService eventStoreService;
  private final UserProjectRepository userProjectRepository;
  private final ProjectMapper projectMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Override
  public EmptyCommandResult handle(UpdateStatusMemberInProjectCommand command,
      ProjectAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var userProject = projectMapper.mapToUserProjectBeforeUpdate(validated.getUserProject(),
        command.getStatus());
    userProjectRepository.save(userProject);

    // storing event
    var event = StatusMemberUpdatedInProjectEvent.eventOf(entityId,
        validated.getLoggedInUser().getId().toString(), validated.getProject().getId().toString(),
        command.getMemberId().toString(), command.getStatus());
    eventStoreService.store(event);

    //send event to queue
    var templateData = new HashMap<String, String>();
    templateData.put("name",
        validated.getMember().getFirstName() + " " + validated.getMember().getLastName());
    String id = sendGridProperties.getDynamicTemplateId().getActivateMemberProject();
    if (command.getStatus().equals(UserProjectStatus.JOINED)) {
      var link =
          sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getProjectDetail()
              .formatted(validated.getProject().getId().toString());
      templateData.put("link", link);
      id = sendGridProperties.getDynamicTemplateId().getDeactivateMemberProject();
    }
    templateData.put("projectName", validated.getProject().getName());
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", validated.getMember().getUsername());

    redisMessagePublisher.publish(
        SendMailMessage.create(validated.getMember().getUsername(), id, templateData));

    // resulting
    return EmptyCommandResult.empty();
  }

}
