package vn.eztek.springboot3starter.task.command.validator.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.comment.entity.Comment;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.comment.DeleteCommentCommand;
import vn.eztek.springboot3starter.task.command.validated.comment.DeleteCommentCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeleteCommentCommandValidator extends
    CommandValidation<DeleteCommentCommand, DeleteCommentCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;
  private final CommentRepository commentRepository;

  @Override
  public DeleteCommentCommandValidated validate(DeleteCommentCommand command) {
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

    var comment = commentRepository.findById(command.getCommentId())
        .orElseThrow(() -> new NotFoundException("comment-not-found"));
    Comment parentComment = null;
    if (comment.getParentCommentId() != null) {
      parentComment = commentRepository.findById(comment.getParentCommentId())
          .orElseThrow(() -> new NotFoundException("parent-comment-not-found"));
    }
    var member = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = member.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(user.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-delete-comment-in-this-project");
    }

    if (!user.getRole().getName().equals(RoleName.PROJECT_MANAGER) &&
        (comment.getUser() == null || !comment.getUser().getId().equals(user.getId()))) {
      throw new AccessDeniedException("you-can-not-delete-comment-in-this-task");
    }

    return DeleteCommentCommandValidated.validatedOf(task, comment, user, parentComment);
  }
}
