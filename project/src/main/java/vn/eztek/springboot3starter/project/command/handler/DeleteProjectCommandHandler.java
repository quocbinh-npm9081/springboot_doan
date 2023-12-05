package vn.eztek.springboot3starter.project.command.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.ProjectDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.DeleteProjectCommand;
import vn.eztek.springboot3starter.project.command.event.ProjectDeletedEvent;
import vn.eztek.springboot3starter.project.command.validator.DeleteProjectCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class DeleteProjectCommandHandler implements
    CommandHandler<DeleteProjectCommand, EmptyCommandResult, ProjectAggregateId> {

  private final ProjectRepository projectRepository;
  private final DeleteProjectCommandValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectEventService projectEventService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final UserProjectRepository userProjectRepository;

  @Override
  public EmptyCommandResult handle(DeleteProjectCommand command, ProjectAggregateId entityId) {

    var validated = validator.validate(command);

    // handling
    var project = validated.getProject();
    project = projectMapper.mapToProjectBeforeUpdateDeleteAt(project,
        DateUtils.currentZonedDateTime());
    projectRepository.save(project);

    var event = ProjectDeletedEvent.eventOf(entityId, project.getId().toString(),
        validated.getUser().getId().toString(), project.getName(), project.getDescription());
    projectEventService.store(event);

    // send event to queue
    List<UserProjectMessage> userProjects = userProjectRepository.findByProjectIdAndUserIdNot(
            project.getId(), validated.getUser().getId()).stream()
        .map(projectMapper::mapToUserProjectMessage).toList();

    redisMessagePublisher.publish(
        ProjectDeleteMessage.create(validated.getProject().getId().toString(),
            validated.getProject().getName(), validated.getUser().getUsername(), userProjects));

    // resulting
    return EmptyCommandResult.empty();
  }

}
