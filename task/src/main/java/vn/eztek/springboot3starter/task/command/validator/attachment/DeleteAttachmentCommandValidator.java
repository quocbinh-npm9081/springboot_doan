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
import vn.eztek.springboot3starter.task.command.attachmnet.DeleteAttachmentCommand;
import vn.eztek.springboot3starter.task.command.validated.attachment.DeleteAttachmentCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeleteAttachmentCommandValidator extends
    CommandValidation<DeleteAttachmentCommand, DeleteAttachmentCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;
  private final AttachmentRepository attachmentRepository;

  @Override
  public DeleteAttachmentCommandValidated validate(DeleteAttachmentCommand command) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(username)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              username));
    }

    var task = taskRepository.findById(command.getTaskId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    var member = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = member.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(user.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-delete-attachment-in-this-project");
    }
    var attachment = attachmentRepository.findById(command.getAttachmentId())
        .orElseThrow(() -> new NotFoundException("attachment-not-found"));

    return DeleteAttachmentCommandValidated.validatedOf(task, attachment, user);
  }
}
