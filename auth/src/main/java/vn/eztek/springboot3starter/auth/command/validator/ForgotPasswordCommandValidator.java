package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ForgotPasswordCommand;
import vn.eztek.springboot3starter.auth.command.validated.ForgotPasswordCommandValidated;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ForgotPasswordCommandValidator
    extends CommandValidation<ForgotPasswordCommand, ForgotPasswordCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public ForgotPasswordCommandValidated validate(ForgotPasswordCommand command) {
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(command.getEmail())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-unverified");
    }

    var oldKey = keyRepository.findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(user.getId(),
        DateUtils.currentZonedDateTime(), KeyType.FORGOT_PASSWORD).orElse(null);

    return ForgotPasswordCommandValidated.validatedOf(user, oldKey);
  }

}
