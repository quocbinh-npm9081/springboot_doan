package vn.eztek.springboot3starter.task.command.validator.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.attachment.repository.AttachmentRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.attachmnet.UpdateAttachmentCommand;
import vn.eztek.springboot3starter.task.command.validated.attachment.UpdateAttachmentCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UpdateAttachmentCommandValidator extends
    CommandValidation<UpdateAttachmentCommand, UpdateAttachmentCommandValidated> {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final AttachmentRepository attachmentRepository;
  private final UserProjectRepository userProjectRepository;

  @Override
  public UpdateAttachmentCommandValidated validate(UpdateAttachmentCommand command) {
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
      throw new AccessDeniedException("you-can-not-update-attachment-in-this-project");
    }
    var attachment = attachmentRepository.findById(command.getAttachmentId())
        .orElseThrow(() -> new NotFoundException("attachment-not-found"));

    if (command.getName().equals(attachment.getName())) {
      throw new BadRequestException("attachment-name-is-same");
    }

    return UpdateAttachmentCommandValidated.validatedOf(loggedInUser, task, attachment);
  }

}
