package vn.eztek.springboot3starter.task.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.query.GetChildrenCommentQuery;
import vn.eztek.springboot3starter.task.query.validated.GetChildrenCommentQueryValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetChildrenCommentQueryValidator extends
    QueryValidation<GetChildrenCommentQuery, GetChildrenCommentQueryValidated> {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final CommentRepository commentRepository;

  @Override
  public GetChildrenCommentQueryValidated validate(GetChildrenCommentQuery query) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var task = taskRepository.findById(query.getTaskId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    var parentComment = commentRepository.findById(query.getParentCommentId())
        .orElseThrow(() -> new NotFoundException("parent-comment-not-found"));

    if (!parentComment.getTaskId().equals(query.getTaskId())) {
      throw new BadRequestException("parent-comment-not-belong-to-this-task");
    }

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var userProjects = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = userProjects.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-are-not-permission-get-comment-in-this-task");
    }

    return GetChildrenCommentQueryValidated.validatedOf();
  }

}
