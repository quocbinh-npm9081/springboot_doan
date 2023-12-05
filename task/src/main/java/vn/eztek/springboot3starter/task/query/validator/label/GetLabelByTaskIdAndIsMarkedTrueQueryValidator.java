package vn.eztek.springboot3starter.task.query.validator.label;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.query.label.GetLabelByTaskIdAndIsMarkedTrueQuery;
import vn.eztek.springboot3starter.task.query.validated.label.GetLabelByTaskIdAndIsMarkedTrueQueryValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetLabelByTaskIdAndIsMarkedTrueQueryValidator extends
        QueryValidation<GetLabelByTaskIdAndIsMarkedTrueQuery, GetLabelByTaskIdAndIsMarkedTrueQueryValidated> {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;

  @Override
  public GetLabelByTaskIdAndIsMarkedTrueQueryValidated validate(GetLabelByTaskIdAndIsMarkedTrueQuery query) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var task = taskRepository.findById(query.getTaskId())
            .orElseThrow(() -> new NotFoundException("task-not-found"));

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
      throw new AccessDeniedException("you-are-not-permission-get-label-in-this-task");
    }

    return GetLabelByTaskIdAndIsMarkedTrueQueryValidated.validatedOf();
  }
}
