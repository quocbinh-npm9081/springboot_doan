package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ChangeEmailCommand;
import vn.eztek.springboot3starter.auth.command.validated.ChangeEmailCommandValidated;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangeEmailCommandValidator
    extends CommandValidation<ChangeEmailCommand, ChangeEmailCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public ChangeEmailCommandValidated validate(ChangeEmailCommand command) {
    var key = keyRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(command.getKey(),
            DateUtils.currentZonedDateTime(), KeyType.REQUEST_CHANGE_EMAIL)
        .orElseThrow(() -> new NotFoundException("invalid-key"));

    var user = userRepository.findByIdAndDeletedAtNull(key.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    var oldKey = keyRepository.findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(user.getId(),
        DateUtils.currentZonedDateTime(), KeyType.UPDATE_STATUS_AFTER_CHANGE_EMAIL).orElse(null);

    return ChangeEmailCommandValidated.validatedOf(user, key, oldKey);
  }

}
