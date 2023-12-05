package vn.eztek.springboot3starter.invitation.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.invitation.query.ListInvitationQuery;
import vn.eztek.springboot3starter.invitation.query.validated.ListInvitationQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ListInvitationQueryValidator extends
    QueryValidation<ListInvitationQuery, ListInvitationQueryValidated> {

  private final UserRepository userRepository;

  @Override
  public ListInvitationQueryValidated validate(ListInvitationQuery query) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    return ListInvitationQueryValidated.validatedOf(loggedInUser);
  }

}
