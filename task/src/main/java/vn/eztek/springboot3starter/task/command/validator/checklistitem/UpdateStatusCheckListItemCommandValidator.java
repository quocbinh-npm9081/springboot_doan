package vn.eztek.springboot3starter.task.command.validator.checklistitem;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.checkListItem.repository.CheckListItemRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.checklistitem.UpdateStatusCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.validated.checklistitem.UpdateStatusCheckListItemCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UpdateStatusCheckListItemCommandValidator extends
    CommandValidation<UpdateStatusCheckListItemCommand, UpdateStatusCheckListItemCommandValidated> {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final UserProjectRepository userProjectRepository;
  private final CheckListItemRepository checkListItemRepository;

  @Override
  public UpdateStatusCheckListItemCommandValidated validate(
      UpdateStatusCheckListItemCommand command) {
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

    var userProjects = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = userProjects.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-update-check-list-item-in-this-task");
    }

    var checkListItem = checkListItemRepository.findById(command.getId())
        .orElseThrow(() -> new NotFoundException("check-list-item-not-found"));

    if (!checkListItem.getCheckList().getId().equals(command.getCheckListId())) {
      throw new BadRequestException("check-list-item-is-not-in-this-check-list");
    }

    if (!checkListItem.getCheckList().getTaskId().equals(command.getTaskId())) {
      throw new BadRequestException("check-list-is-not-in-this-task");
    }

    if (command.getStatus().equals(checkListItem.getIsDone())) {
      throw new BadRequestException("check-list-item-status-is-already-%s".formatted(
          command.getStatus() ? "done" : "undone"));
    }

    return UpdateStatusCheckListItemCommandValidated.validatedOf(loggedInUser, checkListItem);
  }

}
