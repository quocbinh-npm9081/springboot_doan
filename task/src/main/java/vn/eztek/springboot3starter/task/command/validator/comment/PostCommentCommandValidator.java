package vn.eztek.springboot3starter.task.command.validator.comment;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.eztek.springboot3starter.domain.comment.entity.Comment;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.comment.PostCommentCommand;
import vn.eztek.springboot3starter.task.command.validated.comment.PostCommentCommandValidated;


@Component
@RequiredArgsConstructor
public class PostCommentCommandValidator extends
    CommandValidation<PostCommentCommand, PostCommentCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;
  private final CommentRepository commentRepository;

  @Override
  public PostCommentCommandValidated validate(PostCommentCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    Map<String, MultipartFile> fileMap = new HashMap<>();

    var task = taskRepository.findById(command.getTaskId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var memberIds = userProjectRepository.findByProjectId(task.getProject().getId()).stream()
        .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED)).map(x -> x.getUser().getId())
        .toList();

    if (!memberIds.contains(loggedInUser.getId())) {
      throw new AccessDeniedException("you-are-not-permission-post-comment-in-this-task");
    }
    Comment parentComment = null;
    if (command.getParentId() != null) {

      parentComment = commentRepository.findById(command.getParentId())
          .orElseThrow(() -> new NotFoundException("parent-comment-not-found"));

      if (parentComment.getParentCommentId() != null) {
        throw new BadRequestException("cannot-reply-to-this-comment");
      }

      if (!parentComment.getTaskId().equals(command.getTaskId())) {
        throw new BadRequestException("cannot-reply-to-this-comment");
      }
    }

    return PostCommentCommandValidated.validatedOf(loggedInUser, task, parentComment, fileMap);
  }
}
