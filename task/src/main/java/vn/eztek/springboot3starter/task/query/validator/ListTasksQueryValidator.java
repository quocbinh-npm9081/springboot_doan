package vn.eztek.springboot3starter.task.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.query.ListTasksQuery;
import vn.eztek.springboot3starter.task.query.validated.ListTasksQueryValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ListTasksQueryValidator extends
    QueryValidation<ListTasksQuery, ListTasksQueryValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final ProjectRepository projectRepository;

  @Override
  public ListTasksQueryValidated validate(ListTasksQuery query) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var project = projectRepository.findByIdAndDeletedAtNull(query.getProjectId())
        .orElseThrow(() -> new NotFoundException("project-not-found"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (loggedInUser.getStatus().equals(UserStatus.INACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var members = userProjectRepository.findByProjectId(project.getId());
    var memberJoinIds = members.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-are-not-permission-to-view-task-of-this-project");
    }

    return ListTasksQueryValidated.validatedOf();
  }

}
