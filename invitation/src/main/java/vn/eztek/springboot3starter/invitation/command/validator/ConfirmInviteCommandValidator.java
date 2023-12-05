package vn.eztek.springboot3starter.invitation.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ConfirmInviteCommand;
import vn.eztek.springboot3starter.invitation.command.validated.ConfirmInviteCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;


@Component
@RequiredArgsConstructor
public class ConfirmInviteCommandValidator extends
    CommandValidation<ConfirmInviteCommand, ConfirmInviteCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public ConfirmInviteCommandValidated validate(ConfirmInviteCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var invitation = invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(
            command.getKey(), DateUtils.currentZonedDateTime(), InvitationType.INVITE_TALENT_BY_LINK)
        .orElseThrow(() -> new BadRequestException("invitation-invalid"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (!loggedInUser.getRole().getName().equals(RoleName.TALENT)) {
      throw new BadRequestException("user-is-not-talent");
    }

    if (invitation.getProject() == null || invitation.getProject().getDeletedAt() != null) {
      throw new NotFoundException("project-not-found");
    }

    var members = userProjectRepository.findByProjectId(invitation.getProject().getId());
    for (var x : members) {
      if (x.getUser().getId().equals(loggedInUser.getId())) {
        if (x.getStatus().equals(UserProjectStatus.JOINED)) {
          return ConfirmInviteCommandValidated.validatedOf(invitation, loggedInUser,
              invitation.getProject(), true);
        } else {
          throw new BadRequestException("you-are-not-permission-to-join-this-project");
        }
      }
    }
    return ConfirmInviteCommandValidated.validatedOf(invitation, loggedInUser,
        invitation.getProject(), false);
  }
}
