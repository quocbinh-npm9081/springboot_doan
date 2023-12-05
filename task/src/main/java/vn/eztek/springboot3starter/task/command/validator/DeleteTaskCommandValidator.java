package vn.eztek.springboot3starter.task.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.DeleteTaskCommand;
import vn.eztek.springboot3starter.task.command.validated.DeleteTaskCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeleteTaskCommandValidator extends
    CommandValidation<DeleteTaskCommand, DeleteTaskCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;

  @Override
  public DeleteTaskCommandValidated validate(DeleteTaskCommand command) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(username)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              username));
    }

    var task = taskRepository.findById(command.getId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    var member = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = member.stream()
        .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId())
        .toList();

    var isJoinedMember = memberJoinIds.contains(user.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-delete-task-in-this-project");
    }

    Task taskPrevious = null;
    Task taskNext = null;

    if (task.getPreviousId() != null) {
      taskPrevious = taskRepository.findById(task.getPreviousId())
          .orElseThrow(() -> new NotFoundException("previous-task-not-found"));
    }

    if (task.getNextId() != null) {
      taskNext = taskRepository.findById(task.getNextId())
          .orElseThrow(() -> new NotFoundException("next-task-not-found"));
    }

    return DeleteTaskCommandValidated.validatedOf(taskPrevious, task, taskNext, user);
  }
}
