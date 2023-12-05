package vn.eztek.springboot3starter.project.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.project.query.GetSharedLinkQuery;
import vn.eztek.springboot3starter.project.query.validated.GetSharedLinkQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetSharedLinkQueryValidator extends
    QueryValidation<GetSharedLinkQuery, GetSharedLinkQueryValidated> {

  private final UserRepository userRepository;

  @Override
  public GetSharedLinkQueryValidated validate(GetSharedLinkQuery query) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var existUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    return GetSharedLinkQueryValidated.validatedOf(existUser.getId());
  }

}
