package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishSignUpCommand;
import vn.eztek.springboot3starter.auth.command.validated.FinishSignUpCommandValidated;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FinishSignUpCommandValidator
    extends CommandValidation<FinishSignUpCommand, FinishSignUpCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public FinishSignUpCommandValidated validate(FinishSignUpCommand command) {
    var key = keyRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(command.getKey(),
            DateUtils.currentZonedDateTime(), KeyType.ADMIN_CREATE)
        .orElseThrow(() -> new BadRequestException("invalid-key"));

    var user = userRepository.findByIdAndDeletedAtNull(key.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    return FinishSignUpCommandValidated.validatedOf(user, key);
  }

}
