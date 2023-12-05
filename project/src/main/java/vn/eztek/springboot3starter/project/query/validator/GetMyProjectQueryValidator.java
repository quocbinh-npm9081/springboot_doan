package vn.eztek.springboot3starter.project.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.query.GetMyProjectQuery;
import vn.eztek.springboot3starter.project.query.validated.GetMyProjectQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetMyProjectQueryValidator extends
    QueryValidation<GetMyProjectQuery, GetMyProjectQueryValidated> {

  private final UserProjectRepository userProjectRepository;
  private final UserRepository userRepository;

  @Override
  public GetMyProjectQueryValidated validate(GetMyProjectQuery query) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var existUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var listProject = userProjectRepository.findByUserId(existUser.getId());

    return GetMyProjectQueryValidated.validatedOf(listProject);
  }

}
