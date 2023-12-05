package vn.eztek.springboot3starter.task.command.validator.attachment;

import java.nio.file.Paths;
import java.util.UUID;
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
import vn.eztek.springboot3starter.shared.util.ValidUploadAttachment;
import vn.eztek.springboot3starter.task.command.attachmnet.UploadAttachmentCommand;
import vn.eztek.springboot3starter.task.command.validated.attachment.UploadAttachmentCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UploadAttachmentCommandValidator extends
    CommandValidation<UploadAttachmentCommand, UploadAttachmentCommandValidated> {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final AttachmentRepository attachmentRepository;
  private final UserProjectRepository userProjectRepository;

  @Override
  public UploadAttachmentCommandValidated validate(UploadAttachmentCommand command) {
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
      throw new AccessDeniedException("you-can-not-upload-attachment-in-this-project");
    }

    if (!ValidUploadAttachment.checkExtension(command.getAttachment().getOriginalFilename())) {
      throw new BadRequestException("attachment-is-not-valid");
    }

    if (command.getAttachment().getOriginalFilename() == null) {
      throw new BadRequestException("attachment-is-not-valid");
    }

    // check name exist in
    var name = UUID.randomUUID().toString();
    boolean flag = attachmentRepository.existsByNameContainingIgnoreCaseAndTaskId(name,
        command.getTaskId());
    while (flag) {
      name = UUID.randomUUID().toString();
      flag = attachmentRepository.existsByNameContainingIgnoreCaseAndTaskId(name,
          command.getTaskId());
    }

    String originalFilename = command.getAttachment().getOriginalFilename();
    String extension = ValidUploadAttachment.getFileExtension(Paths.get(originalFilename));
    name = name + "." + extension;

    return UploadAttachmentCommandValidated.validatedOf(loggedInUser, task, name);
  }

}
