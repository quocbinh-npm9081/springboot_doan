package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishChangeEmailCommand;
import vn.eztek.springboot3starter.auth.command.validated.FinishChangeEmailCommandValidated;
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
public class FinishChangeEmailCommandValidator
    extends CommandValidation<FinishChangeEmailCommand, FinishChangeEmailCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public FinishChangeEmailCommandValidated validate(FinishChangeEmailCommand command) {
    var key = keyRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(command.getKey(),
            DateUtils.currentZonedDateTime(), KeyType.UPDATE_STATUS_AFTER_CHANGE_EMAIL)
        .orElseThrow(() -> new BadRequestException("invalid-key"));

    var user = userRepository.findByIdAndDeletedAtNull(key.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    return FinishChangeEmailCommandValidated.validatedOf(user, key);
  }

}
