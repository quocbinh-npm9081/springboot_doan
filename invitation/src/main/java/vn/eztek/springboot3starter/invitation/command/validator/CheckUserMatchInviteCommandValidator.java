package vn.eztek.springboot3starter.invitation.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.invitation.command.CheckUserMatchInviteCommand;
import vn.eztek.springboot3starter.invitation.command.validated.CheckUserMatchInviteCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;


@Component
@RequiredArgsConstructor
public class CheckUserMatchInviteCommandValidator extends
    CommandValidation<CheckUserMatchInviteCommand, CheckUserMatchInviteCommandValidated> {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public CheckUserMatchInviteCommandValidated validate(CheckUserMatchInviteCommand command) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var invitation = invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(
            command.getKey(), DateUtils.currentZonedDateTime(), InvitationType.INVITE_TALENT_BY_MAIL)
        .orElseThrow(() -> new BadRequestException("invitation-invalid"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (invitation.getProject() == null || invitation.getProject().getDeletedAt() != null) {
      throw new NotFoundException("project-not-found");
    }

    return CheckUserMatchInviteCommandValidated.validatedOf(loggedInUser, invitation);
  }
}
