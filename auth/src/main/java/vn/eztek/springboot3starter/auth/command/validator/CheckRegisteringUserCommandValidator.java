package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.CheckRegisteringUserCommand;
import vn.eztek.springboot3starter.auth.command.validated.CheckRegisteringUserCommandValidated;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CheckRegisteringUserCommandValidator
    extends CommandValidation<CheckRegisteringUserCommand, CheckRegisteringUserCommandValidated> {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public CheckRegisteringUserCommandValidated validate(CheckRegisteringUserCommand command) {
    var invitation = invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(
            command.getKey(),
            DateUtils.currentZonedDateTime(),
            InvitationType.INVITE_TALENT_BY_MAIL)
        .orElseThrow(() -> new BadRequestException("invalid-invitation"));

    var user = userRepository.findByIdAndDeletedAtNull(invitation.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (user.getStatus().equals(UserStatus.INACTIVE)) {
      throw new NotFoundException("user-not-active");
    }

    if (user.getStatus().equals(UserStatus.REGISTERING)) {
      return CheckRegisteringUserCommandValidated.validatedOf(true, user.getId());
    }

    return CheckRegisteringUserCommandValidated.validatedOf(false, user.getId());
  }

}
