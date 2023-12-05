package vn.eztek.springboot3starter.task.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.stage.repository.StageRepository;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.CreateTaskCommand;
import vn.eztek.springboot3starter.task.command.validated.CreateTaskCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateTaskCommandValidator extends
    CommandValidation<CreateTaskCommand, CreateTaskCommandValidated> {

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final StageRepository stageRepository;
  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;

  @Override
  public CreateTaskCommandValidated validate(CreateTaskCommand command) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var owner = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(username)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!owner.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              username));
    }

    var project = projectRepository.findByIdAndDeletedAtNull(command.getProjectId())
        .orElseThrow(() -> new NotFoundException("project-not-found"));

    var stage = stageRepository.findById(command.getStageId())
        .orElseThrow(() -> new NotFoundException("stage-not-found"));

    var members = userProjectRepository.findByProjectId(project.getId());
    var memberJoinIds = members.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(owner.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-do-not-have-permission-to-create-task-for-this-project");
    }

    Task taskPrevious = null;
    Task taskNext = null;

    if (command.getPreviousId() != null && command.getNextId() != null) {
      taskPrevious = taskRepository.findById(command.getPreviousId())
          .orElseThrow(() -> new NotFoundException("previous-task-not-found"));
      taskNext = taskRepository.findById(command.getNextId())
          .orElseThrow(() -> new NotFoundException("next-task-not-found"));

      if (taskPrevious.getNextId() == null || !taskPrevious.getNextId().equals(taskNext.getId())) {
        throw new BadRequestException("invalid-position");
      }
    }
    // add tail
    if (command.getPreviousId() != null && command.getNextId() == null) {
      taskPrevious = taskRepository.findById(command.getPreviousId())
          .orElseThrow(() -> new NotFoundException("previous-task-not-found"));
      boolean existsByPreviousId = taskRepository.existsByPreviousId(taskPrevious.getId());
      if (existsByPreviousId) {
        throw new BadRequestException("invalid-position");
      }
    }

    // add head
    if (command.getNextId() != null && command.getPreviousId() == null) {
      taskNext = taskRepository.findById(command.getNextId())
          .orElseThrow(() -> new NotFoundException("next-task-not-found"));
      boolean existsByNextId = taskRepository.existsByNextId(taskNext.getId());
      if (existsByNextId) {
        throw new BadRequestException("invalid-position");
      }
    }

    return CreateTaskCommandValidated.validatedOf(owner, stage, project, taskPrevious, taskNext);
  }
}
