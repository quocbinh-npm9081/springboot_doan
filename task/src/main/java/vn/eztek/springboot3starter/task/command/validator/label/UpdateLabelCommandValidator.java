package vn.eztek.springboot3starter.task.command.validator.label;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.label.repository.LabelRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.label.UpdateLabelCommand;
import vn.eztek.springboot3starter.task.command.validated.label.UpdateLabelCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UpdateLabelCommandValidator extends
        CommandValidation<UpdateLabelCommand, UpdateLabelCommandValidated> {
  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final UserProjectRepository userProjectRepository;
  private final LabelRepository labelRepository;

  @Override
  public UpdateLabelCommandValidated validate(UpdateLabelCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
            .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
              "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
                      userName));
    }

    var task = taskRepository.findById(command.getTaskId())
            .orElseThrow(() -> new NotFoundException("task-not-found"));

    var member = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = member.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
            .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-create-label-in-this-task");
    }
    var label = labelRepository.findById(command.getId())
            .orElseThrow(() -> new NotFoundException("label-not-found"));

    if (command.getName().equals(label.getName())) {
      throw new BadRequestException("label-name-is-already-exist");
    }
    return UpdateLabelCommandValidated.validatedOf(loggedInUser, label);
  }
}
