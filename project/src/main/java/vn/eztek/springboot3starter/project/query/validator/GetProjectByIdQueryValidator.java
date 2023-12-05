package vn.eztek.springboot3starter.project.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.query.GetProjectByIdQuery;
import vn.eztek.springboot3starter.project.query.validated.GetProjectByIdQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetProjectByIdQueryValidator extends
    QueryValidation<GetProjectByIdQuery, GetProjectByIdQueryValidated> {

  private final ProjectRepository projectRepository;
  private final UserProjectRepository userProjectRepository;
  private final UserRepository userRepository;

  @Override
  public GetProjectByIdQueryValidated validate(GetProjectByIdQuery query) {

    var project = projectRepository.findByIdAndDeletedAtNull(query.getId())
        .orElseThrow(() -> new NotFoundException("project-not-found"));

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (!loggedInUser.getRole().getName().equals(RoleName.ADMINISTRATOR)) {
      var member = userProjectRepository.findByProjectId(project.getId());
      var memberJoinIds = member.stream()
          .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED)).map(x -> x.getUser().getId())
          .toList();

      var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
      if (!isJoinedMember) {
        throw new AccessDeniedException("you-are-not-permission-to-see-this-project");
      }
    }

    return GetProjectByIdQueryValidated.validatedOf(project);
  }

}
