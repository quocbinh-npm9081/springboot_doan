package vn.eztek.springboot3starter.publicAccess.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.publicAccess.query.GetProjectByKeyQuery;
import vn.eztek.springboot3starter.publicAccess.query.validated.GetProjectByKeyQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetProjectByKeyQueryValidator
    extends QueryValidation<GetProjectByKeyQuery, GetProjectByKeyQueryValidated> {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public GetProjectByKeyQueryValidated validate(GetProjectByKeyQuery query) {
    var invitation = invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(
            query.getKey(),
            DateUtils.currentZonedDateTime(),
            InvitationType.INVITE_TALENT_BY_LINK)
        .orElseThrow(() -> new BadRequestException("invalid-invitation"));

    var user = userRepository.findByIdAndDeletedAtNull(invitation.getInviter().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (user.getStatus().equals(UserStatus.INACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              user.getUsername()));
    }

    return GetProjectByKeyQueryValidated.validatedOf(invitation, user);
  }

}
