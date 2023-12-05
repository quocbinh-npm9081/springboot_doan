package vn.eztek.springboot3starter.task.command.validator.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.comment.UpdateCommentCommand;
import vn.eztek.springboot3starter.task.command.validated.comment.UpdateCommentCommandValidated;


@Component
@RequiredArgsConstructor
public class UpdateCommentCommandValidator extends
    CommandValidation<UpdateCommentCommand, UpdateCommentCommandValidated> {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final CommentRepository commentRepository;

  @Override
  public UpdateCommentCommandValidated validate(UpdateCommentCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var comment = commentRepository.findById(command.getCommentId())
        .orElseThrow(() -> new NotFoundException("comment-not-found"));

    var task = taskRepository.findById(command.getTaskId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (comment.getUser() == null || !comment.getUser().getId().equals(loggedInUser.getId())) {
      throw new AccessDeniedException("you-are-not-permission-update-this-comment-in-this-task");
    }

    return UpdateCommentCommandValidated.validatedOf(loggedInUser, task, comment);
  }
}
