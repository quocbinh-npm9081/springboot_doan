package vn.eztek.springboot3starter.project.command.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.CreateProjectCommand;
import vn.eztek.springboot3starter.project.command.event.ProjectCreatedEvent;
import vn.eztek.springboot3starter.project.command.validator.CreateProjectCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class CreateProjectCommandHandler implements
    CommandHandler<CreateProjectCommand, ProjectResponse, ProjectAggregateId> {

  private final ProjectRepository projectRepository;
  private final UserProjectRepository userProjectRepository;
  private final CreateProjectCommandValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectEventService projectEventService;

  @Override
  public ProjectResponse handle(CreateProjectCommand command, ProjectAggregateId entityId) {

    var validated = validator.validate(command);

    var project = projectMapper.mapToBeforeCreate(command, validated.getUser().getId(),
        ProjectStatus.CREATED);
    var createdProject = projectRepository.save(project);

    var owner = projectMapper.mapToUserProjectBeforeCreate(validated.getUser(), createdProject,
        UserProjectStatus.JOINED);

    userProjectRepository.save(owner);

    var event = ProjectCreatedEvent.eventOf(entityId, createdProject.getId().toString(),
        createdProject.getName(), createdProject.getDescription());

    projectEventService.store(event);
    return projectMapper.mapToProjectResponse(project, UserProjectStatus.JOINED);
  }

}
