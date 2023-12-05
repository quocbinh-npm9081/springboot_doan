package vn.eztek.springboot3starter.project.command.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.ProjectRestoreMessage;
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.RestoreProjectCommand;
import vn.eztek.springboot3starter.project.command.event.ProjectRestoredEvent;
import vn.eztek.springboot3starter.project.command.validator.RestoreProjectCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class RestoreProjectCommandHandler implements
    CommandHandler<RestoreProjectCommand, EmptyCommandResult, ProjectAggregateId> {

  private final ProjectRepository projectRepository;
  private final RestoreProjectCommandValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectEventService projectEventService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final UserProjectRepository userProjectRepository;

  @Override
  public EmptyCommandResult handle(RestoreProjectCommand command, ProjectAggregateId entityId) {

    var validated = validator.validate(command);

    // handling
    var project = validated.getProject();
    project = projectMapper.mapToProjectBeforeUpdateDeleteAt(project, null);
    projectRepository.save(project);

    var event = ProjectRestoredEvent.eventOf(entityId, project.getId().toString(),
        validated.getUser().getId().toString());
    projectEventService.store(event);

    // send event to queue
    List<UserProjectMessage> userProjects = userProjectRepository.findByProjectIdAndUserIdNot(
            project.getId(), validated.getUser().getId()).stream()
        .map(projectMapper::mapToUserProjectMessage).toList();

    redisMessagePublisher.publish(ProjectRestoreMessage.create(validated.getProject().getId(),
        validated.getProject().getName(), validated.getUser().getUsername(),
        validated.getUser().getFirstName() + " " + validated.getUser().getLastName(),
        userProjects));

    // resulting
    return EmptyCommandResult.empty();
  }

}
