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
import vn.eztek.springboot3starter.task.command.label.AssignLabelInTaskCommand;
import vn.eztek.springboot3starter.task.command.validated.label.AssignLabelInTaskCommandValidated;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AssignLabelInTaskCommandValidator extends
        CommandValidation<AssignLabelInTaskCommand, AssignLabelInTaskCommandValidated> {
  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final UserProjectRepository userProjectRepository;
  private final LabelRepository labelRepository;

  @Override
  public AssignLabelInTaskCommandValidated validate(AssignLabelInTaskCommand command) {
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
    Set<UUID> labelIds = new HashSet<>(command.getId());
    var labels = labelRepository.findAllById(labelIds);
    if (labels.size() != labelIds.size()) {
      throw new BadRequestException("invalid-label-list");
    }
    for (var labelId : labelIds) {
      var label = labelRepository.findById(labelId)
              .orElseThrow(() -> new NotFoundException("label-not-found"));
      if (!label.getTaskId().equals(task.getId())) {
        throw new BadRequestException("label-is-do-no-exist-in-this-task");
      }
    }

    return AssignLabelInTaskCommandValidated.validatedOf(labels, loggedInUser);
  }
}
