package vn.eztek.springboot3starter.invitation.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ResponseInviteCommand;
import vn.eztek.springboot3starter.invitation.command.validated.ResponseInviteCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;


@Component
@RequiredArgsConstructor
public class ResponseInviteCommandValidator extends
    CommandValidation<ResponseInviteCommand, ResponseInviteCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public ResponseInviteCommandValidated validate(ResponseInviteCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var invitation = invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(
            command.getId(), DateUtils.currentZonedDateTime(), InvitationType.INVITE_TALENT_BY_MAIL)
        .orElseThrow(() -> new BadRequestException("invitation-invalid"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (!loggedInUser.getId().equals(invitation.getUser().getId())) {
      throw new BadRequestException("you-can-not-response-this-invitation");
    }

    if (invitation.getProject() == null || invitation.getProject().getDeletedAt() != null) {
      throw new NotFoundException("project-not-found");
    }

    var memberIds = userProjectRepository.findByProjectId(invitation.getProject().getId()).stream()
        .map(x -> x.getUser().getId()).toList();

    if (memberIds.contains(loggedInUser.getId())) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-existed-in-this-project".formatted(
              loggedInUser.getUsername()));
    }

    return ResponseInviteCommandValidated.validatedOf(invitation, loggedInUser,
        invitation.getProject());
  }
}
