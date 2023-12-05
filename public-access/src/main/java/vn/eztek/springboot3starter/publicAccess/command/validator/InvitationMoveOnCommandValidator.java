package vn.eztek.springboot3starter.publicAccess.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.publicAccess.command.InvitationMoveOnCommand;
import vn.eztek.springboot3starter.publicAccess.command.validated.InvitationMoveOnCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InvitationMoveOnCommandValidator
    extends CommandValidation<InvitationMoveOnCommand, InvitationMoveOnCommandValidated> {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public InvitationMoveOnCommandValidated validate(InvitationMoveOnCommand command) {
    var invitation = invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(
            command.getKey(),
            DateUtils.currentZonedDateTime(),
            InvitationType.INVITE_TALENT_BY_MAIL)
        .orElseThrow(() -> new BadRequestException("invalid-invitation"));

    var user = userRepository.findByIdAndDeletedAtNull(invitation.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.REGISTERING)) {
      throw new BadRequestException("user-already-registered");
    }

    return InvitationMoveOnCommandValidated.validatedOf(invitation, user);
  }

}
