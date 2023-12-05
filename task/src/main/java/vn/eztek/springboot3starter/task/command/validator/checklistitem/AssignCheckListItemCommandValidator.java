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
import vn.eztek.springboot3starter.task.command.checklistitem.AssignCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.validated.checklistitem.AssignCheckListItemCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AssignCheckListItemCommandValidator extends
    CommandValidation<AssignCheckListItemCommand, AssignCheckListItemCommandValidated> {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final UserProjectRepository userProjectRepository;
  private final CheckListItemRepository checkListItemRepository;

  @Override
  public AssignCheckListItemCommandValidated validate(AssignCheckListItemCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var assignee = userRepository.findById(command.getUserId())
        .orElseThrow(() -> new NotFoundException("assignee-not-found"));

    if (!assignee.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "assignee-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var task = taskRepository.findById(command.getTaskId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    var member = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = member.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-do-not-have-permission-to-assign-check-list-item");
    }

    var isAssigneeJoinedMember = memberJoinIds.contains(assignee.getId());
    if (!isAssigneeJoinedMember) {
      throw new BadRequestException("assignee-is-not-a-member-of-this-project");
    }

    var checkListItem = checkListItemRepository.findById(command.getId())
        .orElseThrow(() -> new NotFoundException("check-list-item-not-found"));

    if (!checkListItem.getCheckList().getId().equals(command.getCheckListId())) {
      throw new BadRequestException("check-list-item-is-not-in-this-check-list");
    }

    if (!checkListItem.getCheckList().getTaskId().equals(command.getTaskId())) {
      throw new BadRequestException("check-list-is-not-in-this-task");
    }

    if (checkListItem.getAssignee() != null && assignee.getId()
        .equals(checkListItem.getAssignee().getId())) {
      throw new BadRequestException("assignee-is-already-assigned");
    }

    return AssignCheckListItemCommandValidated.validatedOf(loggedInUser, assignee, checkListItem);
  }

}
