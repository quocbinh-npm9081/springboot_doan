package vn.eztek.springboot3starter.project.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.project.command.UpdateProjectCommand;
import vn.eztek.springboot3starter.project.command.event.ProjectUpdatedEvent;
import vn.eztek.springboot3starter.project.command.validator.UpdateProjectCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class UpdateProjectCommandHandler implements
    CommandHandler<UpdateProjectCommand, ProjectResponse, ProjectAggregateId> {

  private final ProjectRepository projectRepository;
  private final UpdateProjectCommandValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectEventService projectEventService;

  @Override
  public ProjectResponse handle(UpdateProjectCommand command, ProjectAggregateId entityId) {

    var validated = validator.validate(command);

    // handling
    var project = projectMapper.mapToProjectBeforeUpdate(validated.getProject(), command);
    var updateProject = projectRepository.save(project);
    var event = ProjectUpdatedEvent.eventOf(entityId, updateProject.getId().toString(),
        updateProject.getName(), updateProject.getDescription());
    projectEventService.store(event);

    // resulting
    return projectMapper.mapToProjectResponse(project, UserProjectStatus.JOINED);
  }

}
